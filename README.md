###   BlackGaGa 主要展示了如何快速开发机器人的应用

----------

#### 文档在docs文件下面
#### 相关工具在tools文件夹下面

----------

#### 分别会展示如下：
- 初始化
- 如何获取识别到的文本信息，并且进行解析
- 如何控制机器人的活动
- 如何控制机器人的运动


1. 环境
直接下载项目， 在 Android Studio 中运行即可，无需其他配置，项目结构如下：
- cosclient 模块            ——    基础的链接模块，封装了和底层的连接
- cameraclient 模块         ——    视频连接模块，封装了和底层相机的连接，直接能获取相机数据
- coshandler 模块           ——    cosclient 的封装
- app 模块                  ——    ui和应用
![各个模块之间的简单关系图](https://gitee.com/uploads/images/2018/0406/160216_69dde241_898031.jpeg "blackGaGa.jpg")


2. 如何获取识别到的文本信息
从底层会返回两条重要的消息内容：

SPEECH_ISR_ONLY_RESULT_NTF：这条消息返回了语音识别结果

SPEECH_ISR_LAST_RESULT_NTF：这条消息返回了语义结果    

通过注册监听来获取这两条消息：


> com.csjbot.coshandler.core.Robot#pushSpeech

```
    public void pushSpeech(String json, int type) {
        if (speechListener != null) {
            speechListener.speechInfo(json, type);
        }
    }
```


> com.csjbot.coshandler.core.Robot#setSpeechListener

```

    public void setSpeechListener(OnSpeechListener listener) {
        speechListener = listener;
    }
```


> com.csjbot.blackgaga.core.RobotManager#addListener(com.csjbot.coshandler.listener.OnSpeechListener)
```
    public void addListener(OnSpeechListener speechListener) {
        robot.setSpeechListener(speechListener);
    }
```



示例（com.csjbot.blackgaga.base.BaseFullScreenActivity#addSpeechListener）
```java
 private void addSpeechListener() {
        RobotManager.getInstance().addListener((json, type) -> {
            switch (type) {
                case 0:
                    try {
                        String text = new JSONObject(json).getString("text");

                        if (Constants.Language.isEnglish()) {
                            englishHandle(text);
                        } else if (Constants.Language.isChinese()) {
                            userText = text;
                            if (!mSpeak.isSpeaking()) {
                                isDiscard = false;
                                runOnUiThread(() -> {
                                    if (!isHideUserText()) {
                                        setCustomerChatMsg(text);
                                    }
                                });
                            } else {
                                isDiscard = true;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {

                        if (isDiscard) {
                            return;
                        }

                        if (Constants.Language.isEnglish()) {
                            return;
                        }

                        JSONObject result = new JSONObject(json).getJSONObject("result");
                        String text = result.getString("text");

                        String service = "";
                        try {
                            service = result.getJSONObject("data").getString("service");
                        } catch (Exception e) {
                        }

                        CsjlogProxy.getInstance().info("service:" + service);


                        String answerText = result.getJSONObject("answer").getString("answer_text");

                        if (answerText.contains("#$#$")) {
                            String replaceName = "";
                            if (BuildConfig.robotType.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE)) {
                                replaceName = "爱丽丝";
                            } else if (BuildConfig.robotType.equals(BuildConfig.ROBOT_TYPE_DEF_SNOW)) {
                                replaceName = "小雪";
                            }
                            answerText = answerText.replace("#$#$", replaceName);
                            CsjlogProxy.getInstance().info("answerText:" + answerText);
                        }
                        String filterText = answerText;

                        if (service.equals("CHAT")) {
                            runOnUiThread(() -> onSpeechMessage(text, filterText));
                        } else if (service.equals(MusicBean.service)) {
                            if (isDisableEntertainment()) {
                                return;
                            }
                            String data = result
                                    .getJSONObject("data")
                                    .getJSONObject("data").toString();
                            Bundle bundle = new Bundle();
                            bundle.putString("data", data);
                            if (this.getClass().getSimpleName().equals(MusicActivity.class.getSimpleName())) {
                                runOnUiThread(() -> onShowMessage(data));
                            } else {
                                jumpActivity(MusicActivity.class, bundle);
                                if (this.getClass().getSimpleName().equals(NewsActivity.class.getSimpleName())
                                        || this.getClass().getSimpleName().equals(StoryActivity.class.getSimpleName())
                                        || this.getClass().getSimpleName().equals(DanceActivity.class.getSimpleName())) {
                                    this.finish();
                                }
                            }
                        } else if (service.equals(NewsBean.service)) {
                            if (isDisableEntertainment()) {
                                return;
                            }
                            String data = result
                                    .getJSONObject("data")
                                    .getJSONObject("data").toString();
                            Bundle bundle = new Bundle();
                            bundle.putString("data", data);
                            if (this.getClass().getSimpleName().equals(NewsActivity.class.getSimpleName())) {
                                runOnUiThread(() -> onShowMessage(data));
                            } else {
                                jumpActivity(NewsActivity.class, bundle);
                                if (this.getClass().getSimpleName().equals(MusicActivity.class.getSimpleName())
                                        || this.getClass().getSimpleName().equals(StoryActivity.class.getSimpleName())
                                        || this.getClass().getSimpleName().equals(DanceActivity.class.getSimpleName())) {
                                    this.finish();
                                }
                            }
                        } else if (service.equals("STORY")) {
                            if (isDisableEntertainment()) {
                                return;
                            }
                            String data = result
                                    .getJSONObject("data").toString();
                            Bundle bundle = new Bundle();
                            bundle.putString("data", data);
                            if (this.getClass().getSimpleName().equals(StoryActivity.class.getSimpleName())) {
                                runOnUiThread(() -> onShowMessage(data));
                            } else {
                                jumpActivity(StoryActivity.class, bundle);
                                if (this.getClass().getSimpleName().equals(MusicActivity.class.getSimpleName())
                                        || this.getClass().getSimpleName().equals(NewsActivity.class.getSimpleName())
                                        || this.getClass().getSimpleName().equals(DanceActivity.class.getSimpleName())) {
                                    this.finish();
                                }
                            }
                        } else {
                            runOnUiThread(() -> onSpeechMessage(text, filterText));
                        }
                    } catch (JSONException e) {
                        CsjlogProxy.getInstance().info("json解析出错");
                    }
                    break;
            }
        });
    }

```


3. 机器人的活动：

控制机器人活动的方法都在 > com.csjbot.blackgaga.model.tcp.factory.ServerFactory 里面，直接调用即可


- 机器人左大臂抬起    com.csjbot.coshandler.core.Robot#leftLargeArmUp
- 机器人左大臂放下    com.csjbot.coshandler.core.Robot#leftLargeArmDown