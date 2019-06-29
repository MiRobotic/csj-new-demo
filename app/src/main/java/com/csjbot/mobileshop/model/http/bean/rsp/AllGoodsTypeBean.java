package com.csjbot.mobileshop.model.http.bean.rsp;

import java.util.List;

/**
 * 所有商品品类
 *
 * @author ShenBen
 * @date 2018/12/19 15:26
 * @email 714081644@qq.com
 */

public class AllGoodsTypeBean {

    /**
     * name : 衣服
     * attArray : [{"name":"颜色","labelArray":[{"name":"白色","id":3},{"name":"黑色","id":4},{"name":"蓝色","id":5},{"name":"红色","id":6}],"id":2},{"name":"款式","labelArray":[{"name":"长袖","id":8},{"name":"短袖","id":9},{"name":"衬衫","id":10},{"name":"内衣","id":11}],"id":7},{"name":"季节","labelArray":[{"name":"春季","id":13},{"name":"春夏","id":14},{"name":"夏季","id":15},{"name":"秋季","id":16}],"id":12},{"name":"材料","labelArray":[{"name":"纯棉","id":18},{"name":"合成纤维","id":19},{"name":"皮革","id":20},{"name":"人造棉","id":21}],"id":17}]
     * id : 1
     */

    private String name;
    private int id;
    private List<AttArrayBean> attArray;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<AttArrayBean> getAttArray() {
        return attArray;
    }

    public void setAttArray(List<AttArrayBean> attArray) {
        this.attArray = attArray;
    }

    public static class AttArrayBean {
        /**
         * name : 颜色
         * labelArray : [{"name":"白色","id":3},{"name":"黑色","id":4},{"name":"蓝色","id":5},{"name":"红色","id":6}]
         * id : 2
         */

        private String name;
        private int id;
        private List<LabelArrayBean> labelArray;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<LabelArrayBean> getLabelArray() {
            return labelArray;
        }

        public void setLabelArray(List<LabelArrayBean> labelArray) {
            this.labelArray = labelArray;
        }

        public static class LabelArrayBean {
            /**
             * name : 白色
             * id : 3
             */

            private String name;
            private int id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }
}
