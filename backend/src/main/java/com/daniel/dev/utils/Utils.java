package com.daniel.dev.utils;

import com.daniel.dev.entities.Product;
import com.daniel.dev.projections.ProductProjection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static List<Product> replace(List<ProductProjection> ordered, List<Product> unordered){
        Map<Long, Product> map = new HashMap<>();
        for(Product p : unordered){
            map.put(p.getId(), p);
        }
        List<Product> result = new ArrayList<>();
        for(ProductProjection projection : ordered){
            result.add(map.get(projection.getId()));
        }
        return result;
    }
}
