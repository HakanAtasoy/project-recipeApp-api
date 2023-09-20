package com.atasoy.recipe.service.impl;

import com.atasoy.recipe.entity.*;
import com.atasoy.recipe.entity.enums.Role;
import com.atasoy.recipe.repo.*;
import com.atasoy.recipe.service.InitializerService;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.atasoy.recipe.entity.enums.PreparationTime.BETWEEN_15_AND_30;
import static com.atasoy.recipe.entity.enums.ServingSize.BETWEEN_2_AND_4;

@Service
public class InitializerServiceImpl implements InitializerService {

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final ReviewRepository reviewRepository;

    public InitializerServiceImpl(RecipeRepository recipeRepository, CategoryRepository categoryRepository,
                                  PasswordEncoder passwordEncoder, UserRepository userRepository, FavoriteRepository favoriteRepository, ReviewRepository reviewRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.favoriteRepository = favoriteRepository;
        this.reviewRepository = reviewRepository;
    }

    @EventListener(ContextRefreshedEvent.class)
    @Transactional
    public void initializeSampleData() throws IOException, SQLException {
        // Define Turkish user information
        String[] userFirstNames = {
                "Ahmet", "Mehmet", "Ayşe", "Fatma", "Mustafa", "Emine", "Ali", "Şehrazat", "Osman", "Derya"
        };

        String[] userLastNames = {
                "Yılmaz", "Demir", "Kaya", "Çelik", "Türk", "Aydın", "Bulut", "Erdoğan", "Şahin", "Güneş"
        };

        String[] usernames = {
                "ahmet123", "mehmet45", "ayse_78", "fatma99", "musti_56", "emine_27", "ali.k", "s.shahin", "osman.g", "derya_11"
        };

        String[] passwords = {
                "ahmetpass", "mehmetpass", "aysepass", "fatmapass", "mustifpass", "eminepass", "alipass", "sehrazatpass", "osmanpass", "deryapass"
        };

// Create a list of users
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < userFirstNames.length; i++) {
            User user = new User(userFirstNames[i], userLastNames[i], usernames[i], passwordEncoder.encode(passwords[i]), Role.ROLE_USER);
            userList.add(user);
        }
        userRepository.saveAll(userList);

        var admin = new User("Alex", "De Souza",
                "10_numara", passwordEncoder.encode("1907"), Role.ROLE_ADMIN);

        userRepository.save(admin);

        var admin2 = new User("hakan", "atasoy",
                "stajyer", passwordEncoder.encode("a"), Role.ROLE_ADMIN);

        userRepository.save(admin2);

        String[] turkishCategories = {
                "Çorbalar", "Kebaplar", "Salatalar", "Zeytinyağlılar", "Tatlılar", "Mezeler", "Pilavlar", "Çörekler", "Kahvaltılıklar"
        };

// Create a list of categories
        List<Category> categoryList = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < turkishCategories.length; i++) {
            Category category = new Category();
            category.setName(turkishCategories[i]);
            category.setDescription("Açıklama: " + turkishCategories[i]);

            // For simplicity, let's use the same image for all categories
            ClassPathResource imageResource = new ClassPathResource("static/soup.jpg");
            byte[] imageData = StreamUtils.copyToByteArray(imageResource.getInputStream());
            category.setImageData(imageData);

            categoryList.add(category);
        }

        categoryRepository.saveAll(categoryList);

        // Define Turkish recipe names
        String[] turkishRecipeNames = {
                "Karnıyarık", "İskender Kebap", "Çılbır", "Kumpir", "Baklava", "Dolma", "Melemen", "Kuzu Tandır", "Menemen", "Mantı", "Börek", "İmam Bayıldı", "Hünkar Beğendi", "Adana Kebap", "Ali Nazik", "Tavuk Pilav"
        };

// Create a list of recipes
        List<Recipe> recipeList = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            Recipe recipe = new Recipe();
            recipe.setName(turkishRecipeNames[random.nextInt(turkishRecipeNames.length)]);
            recipe.setDescription("Tarif açıklaması: " + (i + 1));
            recipe.setPreparationTime(BETWEEN_15_AND_30);
            recipe.setServingSize(BETWEEN_2_AND_4);
            recipe.setUserId((i % 10) + 1); // Set user ID
            recipe.setCategoryId((i % turkishCategories.length) + 1); // Set category ID

            // For simplicity, let's use the same image for all recipes
            ClassPathResource imageResource = new ClassPathResource("static/cooked_food.jpg");
            byte[] imageData = StreamUtils.copyToByteArray(imageResource.getInputStream());
            recipe.setImageData(imageData);

            recipeList.add(recipe);
        }

        recipeRepository.saveAll(recipeList);


        List<Favorite> favoriteList = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            for (int j = 1; j <= 4; j++) {
                Favorite favorite = new Favorite();
                favorite.setUserId((i + j) % 10 + 1);
                favorite.setRecipeId(i);
                favoriteList.add(favorite);
            }
        }
        favoriteRepository.saveAll(favoriteList);


        String[] Reviews = {
                "Ailecek yedik bayıldık! Herkese tavsiye ederiz.",
                "İdare eder 5/10. ",
                "Açıkcası bu tarif neden bu kadar popüler anlamadım.",
                "Açlıktan ölmemek için yenir o kadar",
                "Şimdiye kadar bu sitede denediğim en iyi tariflerden gerçekten leziz",
                "Eh işte 6/10.",
                "Bayıldım özellikle sos müthişti.",
                "Bir daha bu yemeği yapmaktansa 5 tane İbahim Tatlıses'in üzerimde halay çekmesini tercih ederim.",
                "Bu şahsın yemekleri hep harika oluyor. Yine hayal kırıklığına uğratmadı. 10/10",
        };

        List<Review> reviewList = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            for(int j = 1; j<= 3; j++) {
                Review review = new Review();
                review.setUserId((i+j) % 10 + 1);
                review.setRecipeId(i % 1000 + 1);
                review.setCreateDate(new Date());
                review.setDescription(Reviews[random.nextInt(Reviews.length)]);
                reviewList.add(review);
            }
        }
        reviewRepository.saveAll(reviewList);
    }

}

