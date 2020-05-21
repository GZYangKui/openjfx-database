package com.openjfx.database.app.controls;

import com.openjfx.database.DataType;
import com.openjfx.database.app.skin.TableColumnTooltipSkin;
import com.openjfx.database.model.TableColumnMeta;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import static com.openjfx.database.app.DatabaseFX.DATABASE_SOURCE;
import static com.openjfx.database.app.utils.AssetUtils.getLocalImage;

/**
 * customer TableColumn
 *
 * @author yangkui
 * @since 1.0
 */
public class TableDataColumn extends TableColumn<ObservableList<StringProperty>, String> {
    /**
     * database table column meta data
     */
    private final TableColumnMeta meta;

    private static final Image LETTER_ICON = getLocalImage(18, 18, "letter-icon.png");
    private static final Image NUMBER_ICON = getLocalImage(18, 18, "number-icon.png");
    private static final Image TIME_ICON = getLocalImage(18, 18, "time-icon.png");
    private static final Image UNKNOWN_ICON = getLocalImage(18, 18, "unknown-icon.png");

    public TableDataColumn(TableColumnMeta meta) {
        this.meta = meta;
        initLabel();
        setText(meta.getField());
    }

    public TableDataColumn(final String field) {
        var m = new TableColumnMeta();
        m.setField(field);
        this.meta = m;
        initLabel();
        setText(field);
    }

    public static final class TableColumnTooltip extends Tooltip {
        private final ObjectProperty<TableColumnMeta> tableColumnMetaObjectProperty = new SimpleObjectProperty<>();

        public TableColumnTooltip(TableColumnMeta meta) {
            tableColumnMetaObjectProperty.set(meta);
        }

        @Override
        protected Skin<?> createDefaultSkin() {
            return new TableColumnTooltipSkin(this);
        }

        public TableColumnMeta getTableColumnMetaObjectProperty() {
            return tableColumnMetaObjectProperty.get();
        }

        public ObjectProperty<TableColumnMeta> tableColumnMetaObjectPropertyProperty() {
            return tableColumnMetaObjectProperty;
        }

        public void setTableColumnMetaObjectProperty(TableColumnMeta tableColumnMetaObjectProperty) {
            this.tableColumnMetaObjectProperty.set(tableColumnMetaObjectProperty);
        }
    }

    private void initLabel() {
        var label = new Label();
        var type = meta.getType();
        final ImageView imageView;
        if (type == null) {
            imageView = new ImageView(UNKNOWN_ICON);
        } else {
            var dataType = DATABASE_SOURCE.getDataType();
            var correctType = dataType.getDataType(type);
            if (dataType.isCategory(correctType, DataType.DataTypeEnum.DATETIME)) {
                imageView = new ImageView(TIME_ICON);
            } else if (dataType.isCategory(correctType, DataType.DataTypeEnum.STRING)) {
                imageView = new ImageView(LETTER_ICON);
            } else {
                imageView = new ImageView(NUMBER_ICON);
            }
        }
        label.setGraphic(imageView);
        setGraphic(label);
        label.setTooltip(new TableColumnTooltip(meta));
    }

    public TableColumnMeta getMeta() {
        return meta;
    }
}
