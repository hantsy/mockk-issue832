package com.example.demo

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient

@OptIn(ExperimentalCoroutinesApi::class)
@WebFluxTest
class PostControllerTest {

    @MockkBean
    lateinit var postClientRepository: PostClientRepository

    @Autowired
    lateinit var client: WebTestClient

    @Test
    fun getPostById() = runTest {
        coEvery { postClientRepository.findById(any()) } returns
                QueryPostByIdResult.Success(
                    Post(id = 1, title = "title1", content = "content1")
                )

        client.get().uri("/posts/1").exchange().expectStatus().isOk

        coVerify { postClientRepository.findById(any()) }
    }
}