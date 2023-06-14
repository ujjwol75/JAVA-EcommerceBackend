package com.example.backend.controller;

import com.example.backend.model.Product;
import com.example.backend.payload.AppConstants;
import com.example.backend.payload.ProductDto;
import com.example.backend.payload.ProductResponse;
import com.example.backend.service.FileUpload;
import com.example.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileUpload fileUpload;


    @Value("${product.path.images}")
    private String imagePath;


    @PostMapping("product/images/{productId}")
    public ResponseEntity<?> uploadImageOfProduct(@PathVariable int productId,
                                                  @RequestParam("product_image") MultipartFile file)
    {
        ProductDto product = this.productService.viewProductById(productId);
        String imageName = null;
        try {
            String uploadImage = this.fileUpload.uploadImage(imagePath, file);
            product.setProduct_imageName(uploadImage);
            ProductDto updateProduct = this.productService.updateProduct(productId, product);

            return new ResponseEntity<>(updateProduct, HttpStatus.ACCEPTED);
        }catch (Exception e){
            e.printStackTrace();
           return new ResponseEntity<>(Map.of("Message", "File Not Uploaded in Server..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    //create Product with category id
    @PostMapping("/create/{id}")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto, @PathVariable int id) {
        ProductDto p = this.productService.createProduct(productDto, id);
        return new ResponseEntity<ProductDto>(p, HttpStatus.CREATED);
    }

    //view All products
    @GetMapping("/view")
    public ProductResponse viewAllProducts(@RequestParam(value = "pageNumber", defaultValue=AppConstants.PAGE_NUMBER_STRING, required=false) int pageNumber,
                                                            @RequestParam(value = "pageSize", defaultValue=AppConstants.PAGE_SIZE_STRING, required=false) int pageSize,
                                                            @RequestParam(value = "sortBy", defaultValue=AppConstants.SORT_BY_STRING, required=false) String sortBy,
                                                            @RequestParam(value = "sortDir", defaultValue=AppConstants.SORT_DIR_STRING, required=false) String sortDir) {


        ProductResponse response = this.productService.viewAll(pageNumber, pageSize, sortBy, sortDir);

        return response;
    }

    //view product by productId
    @GetMapping("/view/{id}")
    public ResponseEntity<ProductDto> viewProductById(@PathVariable("id") int id) {
        ProductDto productDto = this.productService.viewProductById(id);
        return new ResponseEntity<ProductDto>(productDto, HttpStatus.OK);
    }

    //delete Product by productId
    @GetMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        this.productService.deleteProductById(id);
        return new ResponseEntity<String>("Deleted product id ", HttpStatus.OK);
    }

    //update Product By Product Id
    @PutMapping("/update/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable int id, @RequestBody ProductDto newProduct) {
        ProductDto productDto = this.productService.updateProduct(id, newProduct);
        return new ResponseEntity<ProductDto>(productDto, HttpStatus.ACCEPTED);
    }

    // find product by categoriwise
    @GetMapping("/category/{catId}")
    public ResponseEntity<List<ProductDto>> getProductByCat(@PathVariable("catId") int catId){
        List<ProductDto> productByCategory = this.productService.findProductByCategory(catId);
        return new ResponseEntity<List<ProductDto>>(productByCategory, HttpStatus.ACCEPTED);
    }


}
