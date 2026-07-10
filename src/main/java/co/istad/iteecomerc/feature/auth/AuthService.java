package co.istad.iteecomerc.feature.auth;

import co.istad.iteecomerc.feature.auth.dto.RegisterRequest;
import co.istad.iteecomerc.feature.auth.dto.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
}
