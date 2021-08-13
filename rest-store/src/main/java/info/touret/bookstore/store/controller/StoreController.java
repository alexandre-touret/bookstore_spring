package info.touret.bookstore.store.controller;

import info.touret.bookstore.store.dto.BookDTO;
import info.touret.bookstore.store.dto.BookStoreResult;
import info.touret.bookstore.store.service.StoreService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.bouncycastle.util.StoreException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/store")
public class StoreController {

    private StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The Book is bought successfully")})
    @PostMapping
    public ResponseEntity<BookStoreResult> buyABook(BookDTO bookDTO) throws StoreException {
        return null;
    }
}
