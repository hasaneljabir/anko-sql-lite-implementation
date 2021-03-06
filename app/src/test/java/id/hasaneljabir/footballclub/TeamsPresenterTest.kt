package id.hasaneljabir.footballclub

import com.google.gson.Gson
import id.hasaneljabir.footballclub.api.ApiRepository
import id.hasaneljabir.footballclub.api.TheSportDBApi
import id.hasaneljabir.footballclub.fragment.teams.TeamsPresenter
import id.hasaneljabir.footballclub.fragment.teams.TeamsView
import id.hasaneljabir.footballclub.model.Team
import id.hasaneljabir.footballclub.model.TeamResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class TeamsPresenterTest {
    @Mock
    private
    lateinit var view: TeamsView

    @Mock
    private
    lateinit var gson: Gson

    @Mock
    private
    lateinit var apiRepository: ApiRepository

    private lateinit var presenter: TeamsPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = TeamsPresenter(view, apiRepository, gson)
    }

    @Test
    fun testGetTeamList() {
        val teams: MutableList<Team> = mutableListOf()
        val response = TeamResponse(teams)
        val league = "English Premiere League"

        GlobalScope.launch {
            `when`(
                gson.fromJson(
                    apiRepository
                        .doRequest(TheSportDBApi.getTeams(league)).await(),
                    TeamResponse::class.java
                )
            ).thenReturn(response)

            presenter.getTeamList(league)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showTeamList(teams)
            Mockito.verify(view).hideLoading()
        }
    }
}