package vn.vnpt.api.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.vnpt.api.dto.in.address.CreateUpdateAddressIn;
import vn.vnpt.api.dto.in.auth.SignUpRequest;
import vn.vnpt.api.dto.in.auth.SigninRequest;
import vn.vnpt.api.dto.in.review.CreateReviewIn;
import vn.vnpt.api.dto.out.address.AddressDetailOut;
import vn.vnpt.api.dto.out.address.AddressListOut;
import vn.vnpt.api.dto.out.auth.ChangePasswordDtoIn;
import vn.vnpt.api.dto.out.auth.JwtAuthenticationResponse;
import vn.vnpt.api.dto.out.review.ReviewListOut;
import vn.vnpt.api.dto.out.customter.ProfileDetailOut;
import vn.vnpt.api.dto.out.customter.UpdateProfileIn;
import vn.vnpt.api.model.User;
import vn.vnpt.api.repository.CustomerRepository;
import vn.vnpt.api.repository.UserRepository;
import vn.vnpt.api.service.CartService;
import vn.vnpt.api.service.CustomerService;
import vn.vnpt.api.service.EmailService;
import vn.vnpt.authentication.jwt.JwtService;
import vn.vnpt.common.exception.BadRequestException;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final CartService cartService;
    private final EmailService emailService;
    
    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        // Generate verification token
//        String token = UUID.randomUUID().toString();
//        // Send verification email
//        String verificationUrl = "http://localhost:9000/verify?token=" + token;
//        emailService.sendEmail(request.getEmail(), "Verify account",  "Click the link to verify your account: " + verificationUrl);

        var user = User.builder().username(request.getUsername()).firstName(request.getFirstName()).lastName(request.getLastName())
                .email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).phone(request.getPhone()).fullName(request.getFullName())
                .build();
        userRepository.saveUser(user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Tài khoản hoặc mật khẩu không chính xác"));

        var jwt = jwtService.generateToken(user);

        try {
            cartService.loadSessionCartIntoUserCart(user.getId(), request.getSessionToken());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public void createNewAddress(CreateUpdateAddressIn addressRequest) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        customerRepository.createNewAddress(addressRequest, user.get().getId());
    }

    @Override
    public void updateAddress(String addressId, CreateUpdateAddressIn addressRequest) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());

        customerRepository.updateAddress(addressId, addressRequest, user.get().getId());
    }

    @Override
    public void deleteAddress(String addressId) {
        customerRepository.deleteAddress(addressId);
    }

    @Override
    public AddressListOut getDefaultAddress() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());

        return customerRepository.getDefaultAddress(user.get().getId());
    }

    @Override
    public List<AddressListOut> getAllAddress() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());

        return customerRepository.listAllAddress(user.get().getId());
    }

    @Override
    public AddressDetailOut getAddressDetail(String addressId) {
        return customerRepository.getAddressDetail(addressId);
    }

    @Override
    public ProfileDetailOut getCurrentProfile() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());

        return objectMapper.convertValue(user.get(), ProfileDetailOut.class);
    }

    @Override
    public void updateProfile(UpdateProfileIn updateProfileIn) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        customerRepository.updateProfile(updateProfileIn, user.get().getId());
    }

    @Override
    public void createComment(CreateReviewIn createReviewIn) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        customerRepository.createReview(createReviewIn, user.get().getId());
    }

    @Override
    public void deleteComment(String commentId) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());

        customerRepository.deleteReview(commentId, user.get().getId());
    }

    @Override
    public PagingOut<ReviewListOut> getProductComments(String productId, SortPageIn sortPageIn) {
        return customerRepository.getProductReviews(productId, sortPageIn);
    }

    @Override
    public void changePassword(ChangePasswordDtoIn changePasswordDtoIn) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());

        if(passwordEncoder.matches(user.get().getPassword() ,changePasswordDtoIn.getOldPassword())){
                userRepository.changePassword(user.get().getId(), passwordEncoder.encode(changePasswordDtoIn.getNewPassword()));
        }else {
            throw new BadRequestException("Mật khẩu cũ không trùng khớp");
        }
    }
}
