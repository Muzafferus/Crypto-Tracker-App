package com.muzafferus.cryptotrackerapp.data.remote

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CryptoServiceTest {

    private lateinit var service: CryptoService
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoService::class.java)
    }

    private fun enqueueMockResponse(
        fileName: String
    ) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        mockResponse.setBody(source.readString(Charsets.UTF_8))
        server.enqueue(mockResponse)
    }

    @Test
    fun getCryptoList_sentRequest_receivedExpected() {
        runBlocking {
            enqueueMockResponse("cryptoresponse.json")
            val responseBody = service.getCryptoList(false).body()
            val request = server.takeRequest()
            assertThat(responseBody).isNotNull()
            assertThat(request.path).isEqualTo("/v3/coins/list?include_platform=false")
        }
    }

    @Test
    fun getCryptoList_receivedResponse_correctContent() {
        runBlocking {
            enqueueMockResponse("cryptoresponse.json")
            val responseBody = service.getCryptoList(false).body()
            val article = responseBody?.get(0)
            assertThat(article?.id).isEqualTo("01coin")
            assertThat(article?.symbol).isEqualTo("zoc")
            assertThat(article?.name).isEqualTo("01coin")
        }
    }

    @Test
    fun getPrice_sentRequest_receivedExpected() {
        runBlocking {
            enqueueMockResponse("priceresponse.json")
            val responseBody = service.getPrice("01coin", "usd").body()
            val request = server.takeRequest()
            assertThat(responseBody).isNotNull()
            assertThat(request.path).isEqualTo("/v3/simple/price?ids=01coin&vs_currencies=usd")
        }
    }

    @Test
    fun getPrice_receivedResponse_correctContent() {
        runBlocking {
            val cryptoId = "01coin"
            enqueueMockResponse("priceresponse.json")
            val responseBody = service.getPrice(cryptoId, "usd").body()
            assertThat(responseBody?.get(cryptoId)?.usd).isEqualTo(0.00028647.toBigDecimal())
        }
    }

    @Test
    fun getDetail_sentRequest_receivedExpected() {
        runBlocking {
            val cryptoId = "01coin"
            enqueueMockResponse("detailsresponse.json")
            val responseBody = service.getDetail(cryptoId).body()
            val request = server.takeRequest()
            assertThat(responseBody).isNotNull()
            assertThat(request.path).isEqualTo("/v3/coins/01coin")
        }
    }

    @Test
    fun getDetail_receivedResponse_correctContent() {
        runBlocking {
            val cryptoId = "01coin"
            enqueueMockResponse("detailsresponse.json")
            val responseBody = service.getDetail(cryptoId).body()
            assertThat(responseBody?.symbol).isEqualTo("zoc")
            assertThat(responseBody?.name).isEqualTo("01coin")
            assertThat(responseBody?.id).isEqualTo("01coin")
            assertThat(responseBody?.coingecko_rank).isEqualTo(2390)
            assertThat(responseBody?.image?.large).isEqualTo("https://assets.coingecko.com/coins/images/5720/large/F1nTlw9I_400x400.jpg?1547041588")
            assertThat(responseBody?.image?.small).isEqualTo("https://assets.coingecko.com/coins/images/5720/small/F1nTlw9I_400x400.jpg?1547041588")
            assertThat(responseBody?.image?.thumb).isEqualTo("https://assets.coingecko.com/coins/images/5720/thumb/F1nTlw9I_400x400.jpg?1547041588")
        }
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}