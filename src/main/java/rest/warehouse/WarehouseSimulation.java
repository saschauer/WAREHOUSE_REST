package rest.warehouse;

import rest.model.Product;
import rest.model.WarehouseData;

import java.util.ArrayList;
import java.util.List;

public class WarehouseSimulation {
	
	private double getRandomDouble( int inMinimum, int inMaximum ) {

		double number = ( Math.random() * ( (inMaximum-inMinimum) + 1 )) + inMinimum; 
		double rounded = Math.round(number * 100.0) / 100.0; 
		return rounded;
		
	}

	private int getRandomInt( int inMinimum, int inMaximum ) {

		double number = ( Math.random() * ( (inMaximum-inMinimum) + 1 )) + inMinimum; 
		Long rounded = Math.round(number); 
		return rounded.intValue();

	}
	
	public WarehouseData getData( String inID ) {
		
		WarehouseData data = new WarehouseData();
		data.setWarehouseID( inID );
		data.setWarehouseName( "Linz Bahnhof" );

        if("001".equals( inID ) ) {
            data.setWarehouseAddress( "Linz Bahnhof" );
            data.setWarehouseAddress("Bahnhofsstrasse 27/9");
            data.setWarehousePostalCode( "Linz" );
            data.setWarehouseCity( "Linz" );
            data.setWarehouseCountry( "Austria" );

            List<Product> productList = new ArrayList<>();

            Product p1 = new Product();
            p1.setProductID("00-443175");
            p1.setProductName("Bio Orangensaft Sonne");
            p1.setProductCategory("Getraenk");
            p1.setProductQuantity(getRandomInt(1000, 3000));
            p1.setProductUnit("Packung 1L");
            productList.add(p1);

            Product p2 = new Product();
            p2.setProductID("00-871895");
            p2.setProductName("Bio Apfelsaft Gold");
            p2.setProductCategory("Getraenk");
            p2.setProductQuantity(getRandomInt(2500, 4000));
            p2.setProductUnit("Packung 1L");
            productList.add(p2);

            Product p3 = new Product();
            p3.setProductID("01-926885");
            p3.setProductName("Ariel Waschmittel Color");
            p3.setProductCategory("Waschmittel");
            p3.setProductQuantity(getRandomInt(300, 800));
            p3.setProductUnit("Packung 3KG");
            productList.add(p3);

            Product p4 = new Product();
            p4.setProductID("00-316253");
            p4.setProductName("Persil Discs Color");
            p4.setProductCategory("Waschmittel");
            p4.setProductQuantity(getRandomInt(1000, 2000));
            p4.setProductUnit("Packung 700G");
            productList.add(p4);

            data.setProductData(productList);
        }

		return data;
		
	}

}
