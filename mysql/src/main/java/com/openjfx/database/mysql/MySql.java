package com.openjfx.database.mysql;

import com.openjfx.database.base.AbstractDataBasePool;
import com.openjfx.database.base.AbstractDatabaseSource;
import com.openjfx.database.common.VertexUtils;
import com.openjfx.database.model.ConnectionParam;

import com.openjfx.database.mysql.impl.MysqlPoolImpl;
import io.vertx.mysqlclient.MySQLPool;

import java.util.Objects;

/**
 * mysql数据库连接池管理
 *
 * @author yangkui
 * @since 1.0
 */
public class MySql extends AbstractDatabaseSource {

    {
        heartBeat();
    }

    @Override
    public AbstractDataBasePool createPool(ConnectionParam params) {
        var mySqlPool = MysqlHelper.createPool(params);
        var pool = MysqlPoolImpl.create(mySqlPool);
        pool.setConnectionParam(params);
        //确保链接可用后在加入缓存之中
        pool.getDql().heartBeatQuery()
                .onSuccess(r -> pools.put(params.getUuid(), pool));
        return pool;
    }

    @Override
    public void close(String uuid) {
        var pool = pools.get(uuid);
        if (Objects.nonNull(pool)) {
            pool.close();
        }
        //移出数据库连接缓存
        pools.remove(uuid);
    }

    @Override
    public void closeAll() {
        pools.forEach((key, pool) -> {
            pool.close();
        });
        //清空数据库缓存
        pools.clear();
        //关闭定时器
        if (timerId != null) {
            VertexUtils.getVertex().cancelTimer(timerId);
        }
    }

    @Override
    public void heartBeat() {
        //每隔10s向mysql服务器发送一次sql查询语句
        VertexUtils.getVertex().setPeriodic(10000, timer -> {
            pools.forEach((a, b) -> {
                var future = b.getDql().heartBeatQuery();
                var serverName = b.getConnectionParam().getName();
                var host = b.getConnectionParam().getHost();
                var target = serverName + "<" + host + ">";
                future.onSuccess(ar -> {
                    System.out.println("success heart beat to " + target + " server");
                });
                future.onFailure(t -> {
                    System.out.println("failed heart beat to " + target + " server cause:" + t.getMessage());
                });
            });
        });
    }
}
