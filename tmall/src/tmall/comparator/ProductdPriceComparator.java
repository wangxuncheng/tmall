package tmall.comparator;
 
import java.util.Comparator;
 
import tmall.bean.Product;
 
public class ProductdPriceComparator implements Comparator<Product>{
 
    @Override
    public int compare(Product p1, Product p2) {
        return (int) (p2.getPromotePrice()-p1.getPromotePrice());
    }
 
}