package com.example.backend.service;

import com.example.backend.Exception.ResourceNotFoundException;
import com.example.backend.model.Category;
import com.example.backend.model.Product;
import com.example.backend.payload.CategoryDto;
import com.example.backend.payload.ProductDto;
import com.example.backend.payload.ProductResponse;
import com.example.backend.repository.CategoryRepository;
import com.example.backend.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    public ProductDto createProduct(ProductDto productDto, int id) {
        System.out.println("id:: " + id);
        //fetched category is availabel or not
        Category category = this.categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("This category id is not found..."));
        System.out.println("Category:: " + category);

        //productDto to entity,
        Product p = toEntity(productDto);

        p.setCategory(category);

        //save product in database
        Product product = this.productRepo.save(p);

        //change product to productDto
        ProductDto save = toDto(product);

        return save;
    }


    public ProductResponse viewAll(int pageNumber, int pageSize, String sortBy, String sortDir) {


        System.out.println("pageNumber :: " + pageNumber);
        System.out.println("pageSize :: " + pageSize);
        System.out.println("sortBy :: " + sortBy);
        System.out.println("sortDir :: " + sortDir);
        Sort sort = null;
        if (sortDir.trim().toLowerCase().equals("asc")) {
            sort = Sort.by(sortBy).ascending();
        }
        else {
            sort = Sort.by(sortBy).descending();
        }

        System.out.println("sort:: "+ sort);

        // I think automatic sort hunxa products according to product_id
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        System.out.println("pageable:: "+ pageable);
        Page<Product> page = this.productRepo.findAll(pageable);
        System.out.println("Page:: "+page);
        List<Product> pageProduct = page.getContent();
        System.out.println("Page Content: "+ pageProduct);
//        List<Product> product = pageProduct.stream().filter(p -> p.getLive()).collect(Collectors.toList());
//        System.out.println("Product:: "+ product);
        List<ProductDto> productDto = pageProduct.stream().map(p -> this.toDto(p)).collect(Collectors.toList());
        System.out.println("productDto::"+ productDto);
        ProductResponse response = new ProductResponse();
        response.setContent(productDto);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());


//        List<Product> all = this.productRepo.findAll();
//        //product to productDto
//        List<ProductDto> findAllDto = all.stream().map(product -> this.toDto(product)).collect(Collectors.toList());
        return response;
    }

    public ProductDto viewProductById(int id) {
        Product productById = this.productRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(+id + "From this Id Product Not Found!"));

        ProductDto productDto = toDto(productById);
        return productDto;
    }

    public void deleteProductById(int id) {
        Product byId = productRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(id + " This is resound is not found!"));
        productRepo.delete(byId);

    }

    public ProductDto updateProduct(int id, ProductDto newProduct) {
        Product oldProduct = this.productRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(+id + "From this Id Product Not Found!"));
        oldProduct.setProduct_desc(newProduct.getProduct_desc());
        oldProduct.setProduct_name(newProduct.getProduct_name());
        oldProduct.setProduct_price(newProduct.getProduct_price());
        oldProduct.setProduct_quantity(newProduct.getProduct_quantity());
        oldProduct.setProduct_imageName(newProduct.getProduct_imageName());
        oldProduct.setLive(newProduct.isLive());
        oldProduct.setStock(newProduct.isStock());


        Product save = this.productRepo.save(oldProduct);

        ProductDto productDto = toDto(save);
        return productDto;
    }

    // ProductDto to entity
    public Product toEntity(ProductDto productDto) {

        Product p = new Product();
        p.setProduct_name(productDto.getProduct_name());
        p.setProduct_id(productDto.getProduct_id());
        p.setProduct_price(productDto.getProduct_price());
        p.setProduct_quantity(productDto.getProduct_quantity());
        p.setProduct_desc(productDto.getProduct_desc());
        p.setLive(productDto.isLive());
        p.setStock(productDto.isStock());
        p.setProduct_imageName(productDto.getProduct_imageName());

        return p;
    }

    //entity to ProductDto
    public ProductDto toDto(Product product) {

        ProductDto productDto = new ProductDto();
        productDto.setProduct_id(product.getProduct_id());
        productDto.setProduct_name(product.getProduct_name());
        productDto.setProduct_price(product.getProduct_price());
        productDto.setProduct_desc(product.getProduct_desc());
        productDto.setLive(product.getLive());
        productDto.setStock(product.isStock());
        productDto.setProduct_quantity(product.getProduct_quantity());
        productDto.setProduct_imageName(product.getProduct_imageName());

        //set cateogory to categoryDto
        if (product.getCategory() != null) {
            CategoryDto catDto = new CategoryDto();
            catDto.setCategoryId(product.getCategory().getCategoryId());
            catDto.setTitle(product.getCategory().getTitle());
            productDto.setCategoryDto(catDto);
        }

        //then set categoryDto to category
//        productDto.setCategory(catDto);

        return productDto;
    }

    //find product by category
    public List<ProductDto> findProductByCategory(int id){
       Category category = this.categoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("this id category is not found..."));
        List<Product> findByCategory = this.productRepo.findByCategory(category);
        List<ProductDto> collect = findByCategory.stream().map(product -> toDto(product)).collect(Collectors.toList());
        return collect;
    }


}
