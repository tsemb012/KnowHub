package com.tsemb.droidsoftthird

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tsemb.droidsoftthird.composable.event.EventDetailScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//TODO EventDetailFragmentにした方が良い。
@AndroidEntryPoint
class ScheduleDetailFragment: Fragment() {

    @Inject
    lateinit var viewModelAssistedFactory: ScheduleDetailViewModel.Factory

    private val eventId by lazy { ScheduleDetailFragmentArgs.fromBundle(requireArguments()).eventId }

    private val viewModel by lazy { viewModelAssistedFactory.create(eventId) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.fetchEventDetail()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.message.let { message ->
            if (message.value != null) {
                Toast.makeText(requireContext(), message.value, Toast.LENGTH_SHORT).show()
            }
        }

        return ComposeView(requireContext()).apply {
            setContent {
                EventDetailScreen(
                        event = viewModel.eventDetail,
                        isLoading = viewModel.isLoading,
                        isJoined = viewModel.isJoined,
                        startVideoChat = { startVideoChat() },
                        deleteEvent = { viewModel.deleteEvent() },
                        joinEvent = { viewModel.joinEvent() },
                        leaveEvent = { viewModel.leaveEvent() },
                        navigateToGroupDetail = { navigateToGroupDetail() },
                ) { findNavController().navigateUp() }
            }
        }
    }

    private fun startVideoChat() {
        viewModel.eventDetail.value?.let {
            val intent = Intent(requireActivity(), VideoChatActivity::class.java)
            intent.putExtra("roomId", it.roomId)
            startActivity(intent)
        }
    }

    private fun navigateToGroupDetail() {
        viewModel.eventDetail.value?.let {
            val action =
                ScheduleDetailFragmentDirections.actionScheduleDetailFragmentToGroupDetailFragment(
                    it.groupId
                )
            findNavController().navigate(action)
        }
    }
}
/*
@Preview
@Composable
fun PreviewScheduleDetailScreen() {
    val event = EventDetail(
        eventId = "1",
        hostId = "1",
        roomId = "1",
        name = "name",
        comment = "comment",
        startDateTime = Date().toInstant().atZone(ZoneId.systemDefault()),
        endDateTime =  Date().toInstant().atZone(ZoneId.systemDefault()),
        place = null,
        groupId = "1",
        groupName = "groupName",
        registeredUserIds = listOf("1", "2"),
        groupMembers = listOf(
            SimpleUser(
                userId = "1",
                userName = "userName",
                userImage = "",
            ),
            SimpleUser(
                userId = "2",
                userName = "userName",
                userImage = "",

                ),
            SimpleUser(
                userId = "3",
                userName = "userName",
                userImage = "",
            ),

            ),
        status = EventStatus.AFTER_REGISTRATION_DURING_EVENT,
        isOnline = false,
    )
    EventDetailScreen(
            event = mutableStateOf(event),
            isLoading = mutableStateOf(false),
            startVideoChat = {},
            deleteEvent = {}
    ) {}
}*/

/*data class EventDetail(
    val eventId: String,
    val hostId: String,
    val roomId: String,
    val name: String,
    val comment: String,
    val startDateTime: ZonedDateTime,
    val endDateTime: ZonedDateTime,
    val place: EditedPlace?,
    val groupId: String,
    val groupName: String,
    val registeredUserIds: List<String>,
    val groupMembers: List<SimpleUser>,
    val status: EventStatus,
    val isOnline: Boolean,
)*/
