package cheolppochwippo.oe_mos_nae_mas_market.domain.user.controller;

import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.UserRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.UserResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.UserUpdateRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.service.UserService;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.userDetails.UserDetailsImpl;
import cheolppochwippo.oe_mos_nae_mas_market.global.common.CommonResponse;
import cheolppochwippo.oe_mos_nae_mas_market.global.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User API", description = "유저 API")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "회원가입", description = "회원가입")
    @PostMapping("/auth/signup")
    public ResponseEntity<CommonResponse<UserResponse>> signup(@RequestBody UserRequest userRequest)
        throws ExecutionException, InterruptedException {
        UserResponse signupedUser = userService.signup(userRequest).get();
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<UserResponse>builder()
                .msg("signup complete!")
                .data(signupedUser)
                .build());
    }

    @Operation(summary = "로그인", description = "로그인")
    @PostMapping("/auth/login")
    public ResponseEntity<CommonResponse<UserResponse>> login(@RequestBody UserRequest userRequest,
        HttpServletResponse response)
        throws ExecutionException, InterruptedException {
        UserResponse loginedUser = userService.login(userRequest).get();
        response.setHeader(JwtUtil.AUTHORIZATION_HEADER,
            jwtUtil.createToken(loginedUser.getUserId(), loginedUser.getUsername(),
                loginedUser.getRole()));
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<UserResponse>builder()
                .msg("login complete!")
                .data(loginedUser)
                .build());
    }

    @Operation(summary = "내 정보 보기", description = "내 정보 보기")
    @GetMapping("/auth/mypage")
    public ResponseEntity<CommonResponse<UserResponse>> mypage(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserResponse mypage = userService.showMypage(userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<UserResponse>builder()
                .msg("get mypage complete!")
                .data(mypage)
                .build());
    }

    @Operation(summary = "내 정보 수정", description = "내 정보 수정")
    @PatchMapping("/auth/mypage")
    public ResponseEntity<CommonResponse<UserResponse>> updateMypage(
        @RequestBody UserUpdateRequest userRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserResponse mypage = userService.updateMypage(userRequest, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<UserResponse>builder()
                .msg("update mypage complete!")
                .data(mypage)
                .build());
    }

    //유저 역할 변경
    @Operation(summary = "유저 역할 변경", description = "유저 역할 변경")
    @PatchMapping("/auth/role")
    public ResponseEntity<CommonResponse<UserResponse>> updateRole(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserResponse userRole = userService.roleUpdate(userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<UserResponse>builder()
                .msg("update role complete!")
                .data(userRole)
                .build());
    }

    @Operation(summary = "사용자 테스트용 회원가입", description = "사용자 테스트용 회원가입")
    @PostMapping("/auth/signup/userTest")
    public ResponseEntity<CommonResponse<UserResponse>> signupByUserTest(@RequestBody UserRequest userRequest)
        throws ExecutionException, InterruptedException {
        UserResponse signupedUser = userService.signupByUserTest(userRequest).get();
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<UserResponse>builder()
                .msg("signupByUserTest complete!")
                .data(signupedUser)
                .build());
    }

    @Operation(summary = "사용자 테스트용 역할 변경", description = "사용자 테스트용 역할 변경")
    @PostMapping("/auth/changeRole")
    public ResponseEntity<CommonResponse<String>> changeRole(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userService.changeRole(userDetails.getUser().getId());
        String jwt = jwtUtil.createToken(user.getId(),user.getUsername(),user.getRole());
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<String>builder()
                .msg("changeRol complete!")
                .data(jwt)
                .build());
    }

}
