package com.example.demo.service;

import com.example.demo.dto.CardDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.model.Card;
import com.example.demo.model.Profile;
import com.example.demo.model.ProfileCard;
import com.example.demo.model.User;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.ProfileCardRepository;
import com.example.demo.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CardService {

    private final UserService userService;
    private final CardRepository cardRepository;
    private final ProfileService profileService;
    private final ProfileCardRepository profileCardRepository;
    private final ProfileRepository profileRepository;


    public ResponseDTO createCard(CardDTO cardDTO) {
        log.info("Creating card with details: {}", cardDTO);
        try {
            Optional<Card> existingCard = cardRepository.findByCardNumber(cardDTO.getCardNumber());
            if (existingCard.isPresent()) {
                log.warn("Card with number {} already exists", cardDTO.getCardNumber());
                throw new RuntimeException("Card with this number already exists");
            }

            Optional<User> user = userService.getAuthenticatedUser();

            List<Profile> profiles = profileRepository.findProfilesByUserId(user.get().getUserId());
            Profile profileExisting = profiles.isEmpty()
                    ? null
                    : profiles.get(profiles.size() - 1);
            if (profileExisting == null) {
                try {
                    profileExisting = profileService.createProfile(user.get().getUserId());
                } catch (Exception e) {
                    log.error("Failed to create profile for user: {}", e.getMessage());
                    throw new RuntimeException("Failed to create profile for user: " + e.getMessage());
                }
            }

            Card card = new Card.Builder()
                    .cardNumber(Optional.ofNullable(cardDTO.getCardNumber())
                            .orElseThrow(() -> new RuntimeException("Card number is required")))
                    .cardExpiryDate(Optional.ofNullable(cardDTO.getExpirationDate())
                            .orElseThrow(() -> new RuntimeException("Card expiration date is required")))
                    .cvv(Optional.ofNullable(cardDTO.getCvv())
                            .orElseThrow(() -> new RuntimeException("Card CVV is required")))
                    .nameOfCard(Optional.ofNullable(cardDTO.getCardName())
                            .orElseThrow(() -> new RuntimeException("Card name is required")))
                    .build();

            log.info("Card object created: {}", card);

            Card savedCard = cardRepository.save(card);
            log.info("Card created successfully: {}", savedCard);

            ProfileCard profileCard = new ProfileCard.Builder()
                    .profileId(profileExisting.getId())
                    .cardId(savedCard.getId())
                    .build();

            profileCardRepository.save(profileCard);


            return new ResponseDTO.Builder()
                    .message("Card created successfully")
                    .status("200")
                    .data(true)
                    .build();

        } catch (Exception e) {
            log.error("Failed to create card: {}", e.getMessage());
            throw new RuntimeException("Failed to create card: " + e.getMessage());
        }
    }
}

