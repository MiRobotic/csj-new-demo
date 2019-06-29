@echo off
gradlew :voiceclient:clean :cosclient:clean :cameraclient:clean  :voiceclient:assemble   :cosclient:assemble :cameraclient:assemble :voiceclient:copyMethod   :cosclient:copyMethod :cameraclient:copyMethod

pause

