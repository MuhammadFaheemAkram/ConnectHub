package co.bitfuse.connecthub.data.auth

interface AuthApiService {
    suspend fun login(email: String, password: String): AuthDto
    suspend fun signUp(name: String, email: String, password: String): AuthDto
}
