package com.openjfx.database.app.model.impl;

import com.openjfx.database.app.model.BaseTabMode;

/**
 * tab数据
 *
 * @author yangkui
 * @since 1.0
 */
public class TableTabModel extends BaseTabMode {
    /**
     * 数据库名称
     */
    private final String database;
    /**
     * 表名
     */
    private final String tableName;

    public TableTabModel(String uuid, String database, String tableName) {
        this.uuid = uuid;
        this.database = database;
        this.tableName = tableName;
    }

    public String getUuid() {
        return uuid.split("_")[0];
    }

    public String getRawUUid(){
        return uuid;
    }

    public String getDatabase() {
        return database;
    }

    public String getTable() {
        return  database+"."+tableName;
    }
    public String getTableName(){
        return tableName;
    }
}
