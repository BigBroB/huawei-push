#### 说明
本工具为华为推送 java 服务器端发送工具

[官方文档](https://developer.huawei.com/consumer/cn/service/hms/catalog/huaweipush_agent.html?page=hmssdk_huaweipush_devguide_server_agent)

#### 使用

##### 示例
```
    SendClient sender = new SendClient("clientSecret","clientId");
   
    Payload payload = Payload.newInstance()
                                    .addMsg(Message.newInstance()
                                                        .addType(MessageType.PASSTHROUGH)
                                                        .addBody("测试推送content", "测试推送title")
                                                        .addAction(ActionType.URL, "https://www.baidu.com")
                                                    .build())
                                    .addExt(Ext.newInstance()
                                                    .addCustomize(map)
                                                .build())
                            .build();

  sender.push(new String[] { "targets" }, payload);

```