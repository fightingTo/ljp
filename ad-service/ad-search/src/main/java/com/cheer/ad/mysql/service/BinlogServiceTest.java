package com.cheer.ad.mysql.service;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;

import java.io.IOException;

/**
 * 测试bin-log服务
 *
 * @Created by ljp on 2019/11/22.
 */
public class BinlogServiceTest {
    public static void main(String[] args) throws IOException {
        BinaryLogClient client = new BinaryLogClient("127.0.0.1", 3306, "root", "123456");
//        client.setBinlogFilename();
//        client.setBinlogPosition();
        client.registerEventListener(event -> {
            EventData data = event.getData();

            if (data instanceof UpdateRowsEventData) {
                System.out.println(data.toString());
            } else if (data instanceof WriteRowsEventData) {
                System.out.println(data.toString());
            } else if (data instanceof DeleteRowsEventData){
                System.out.println(data.toString());
            }
        });

        client.connect();
    }
}
