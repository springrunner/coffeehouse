package coffeehouse.modules.catalog.data.convert;

import coffeehouse.libraries.base.convert.spring.support.ObjectIdConverters;
import coffeehouse.modules.catalog.domain.CategoryId;
import coffeehouse.modules.catalog.domain.ProductId;
import coffeehouse.modules.catalog.domain.ProductItemId;
import coffeehouse.modules.catalog.domain.StockKeepingUnitId;

import java.util.Arrays;
import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class CatalogConverters {

    public static List<?> converters() {
        return Arrays.asList(
                ObjectIdConverters.objectIdToString(CategoryId.class),
                ObjectIdConverters.stringToObjectId(CategoryId.class),
                ObjectIdConverters.objectIdToString(ProductId.class),
                ObjectIdConverters.stringToObjectId(ProductId.class),
                new ProductCodeConverters.ProductCodeToStringConverter(),
                new ProductCodeConverters.StringToProductCodeConverter(),
                ObjectIdConverters.objectIdToString(ProductItemId.class),
                ObjectIdConverters.stringToObjectId(ProductItemId.class),
                ObjectIdConverters.objectIdToString(StockKeepingUnitId.class),
                ObjectIdConverters.stringToObjectId(StockKeepingUnitId.class)
        );
    }
}
