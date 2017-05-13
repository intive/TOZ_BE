package com.intive.patronage.toz.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intive.patronage.toz.comment.model.db.Comment;
import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.environment.ApiProperties;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(DataProviderRunner.class)
@SpringBootTest(
        properties = ApiProperties.JWT_SECRET_BASE64
)
public class CommentControllerTest {
    private static final int COMMENTS_LIST_SIZE = 5;
    private static final UUID EXPECTED_ID = UUID.randomUUID();
    private static final UUID EXPECTED_PET_UUID = UUID.randomUUID();
    private static final UUID DEFAULT_PET_UUID = null;
    private static final Comment.State EXPECTED_STATE = Comment.State.ACTIVE;
    private static final String EXPECTED_CONTENTS = "Very nice dog! I hope that he will " +
            "find new home very, very soon because he deserves to get new home and loving " +
            "owners. Very nice dog! I hope that he will find new home very, very soon " +
            "because he deserves to get new home and loving owners.";
    private static final String EXPECTED_SHORTENED_CONTENTS = "Very nice dog! I hope that " +
            "he will find new home very, very soon because he deserves to get new home and " +
            "loving owners. Very nice dog! I hope that he will find new home very, very soon " +
            "because he deserves to get new ";
    private static final UUID EXPECTED_USER_UUID = UUID.randomUUID();
    private static final Boolean DEFAULT_SHORTENED = false;
    private static final String DEFAULT_STATE = "ACTIVE";
    private static final Boolean SHORTENED_FOR_TEST = true;
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

    @Mock
    private CommentService commentService;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new CommentController(commentService))
                .build();
    }

    @DataProvider
    public static Object[] getProperComment() {
        Comment comment = new Comment();
        comment.setId(EXPECTED_ID);
        comment.setPetUuid(EXPECTED_PET_UUID);
        comment.setContents(EXPECTED_CONTENTS);
        comment.setUserUuid(EXPECTED_USER_UUID);
        comment.setState(EXPECTED_STATE);
        return new Comment[]{comment};
    }

    @Test
    public void getAllComments() throws Exception {
        final List<Comment> commentList = getCommentList(DEFAULT_PET_UUID, DEFAULT_SHORTENED);
        when(commentService.findAllComments(DEFAULT_PET_UUID, DEFAULT_SHORTENED,
                DEFAULT_STATE)).thenReturn(commentList);

        mockMvc.perform(get(ApiUrl.COMMENTS_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$[0].contents", is(EXPECTED_CONTENTS)))
                .andExpect(jsonPath("$", hasSize(COMMENTS_LIST_SIZE)));

        verify(commentService, times(1))
                .findAllComments(DEFAULT_PET_UUID, DEFAULT_SHORTENED, DEFAULT_STATE);
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void getAllCommentsByPetUuid() throws Exception {
        final List<Comment> commentList = getCommentList(EXPECTED_PET_UUID,
                DEFAULT_SHORTENED);
        when(commentService.findAllComments(EXPECTED_PET_UUID, DEFAULT_SHORTENED,
                DEFAULT_STATE)).thenReturn(commentList);

        mockMvc.perform(get(ApiUrl.COMMENTS_PATH).param("petUuid", EXPECTED_PET_UUID
                .toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$[0].petUuid", is(EXPECTED_PET_UUID
                        .toString())))
                .andExpect(jsonPath("$[1].petUuid", is(EXPECTED_PET_UUID
                        .toString())))
                .andExpect(jsonPath("$", hasSize(COMMENTS_LIST_SIZE)));

        verify(commentService, times(1))
                .findAllComments(EXPECTED_PET_UUID, DEFAULT_SHORTENED, DEFAULT_STATE);
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void getAllCommentsShortened() throws Exception {
        final List<Comment> commentList = getCommentList(DEFAULT_PET_UUID, SHORTENED_FOR_TEST);
        when(commentService.findAllComments(DEFAULT_PET_UUID, SHORTENED_FOR_TEST,
                DEFAULT_STATE)).thenReturn(commentList);

        mockMvc.perform(get(ApiUrl.COMMENTS_PATH).param("shortened",
                SHORTENED_FOR_TEST.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$[0].contents",
                        is(EXPECTED_SHORTENED_CONTENTS)))
                .andExpect(jsonPath("$[1].contents",
                        is(EXPECTED_SHORTENED_CONTENTS)))
                .andExpect(jsonPath("$", hasSize(COMMENTS_LIST_SIZE)));

        verify(commentService, times(1))
                .findAllComments(DEFAULT_PET_UUID, SHORTENED_FOR_TEST,
                        DEFAULT_STATE);
        verifyNoMoreInteractions(commentService);
    }

    private List<Comment> getCommentList(UUID petUuid, Boolean shortened) {
        final List<Comment> commentList = new ArrayList<>();
        for (int i = 0; i < COMMENTS_LIST_SIZE; i++) {
            Comment comment = new Comment();
            comment.setUserUuid(EXPECTED_USER_UUID);
            comment.setPetUuid(UUID.randomUUID());
            comment.setState(EXPECTED_STATE);
            if (!shortened) {
                comment.setContents(EXPECTED_CONTENTS);
            } else {
                comment.setContents(EXPECTED_SHORTENED_CONTENTS);
            }
            if (petUuid != null && petUuid.toString().equals(EXPECTED_PET_UUID
                    .toString())) {
                comment.setPetUuid(EXPECTED_PET_UUID);
            } else {
                comment.setPetUuid(UUID.randomUUID());
            }
            commentList.add(comment);
        }
        return commentList;
    }

    @Test
    @UseDataProvider("getProperComment")
    public void getCommentById(final Comment comment) throws Exception {
        when(commentService.findById(EXPECTED_ID)).thenReturn(comment);
        mockMvc.perform(get(String.format("%s/%s", ApiUrl.COMMENTS_PATH, EXPECTED_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.id", is(EXPECTED_ID.toString())))
                .andExpect(jsonPath("$.petUuid", is(EXPECTED_PET_UUID
                        .toString())))
                .andExpect(jsonPath("$.contents", is(EXPECTED_CONTENTS)))
                .andExpect(jsonPath("$.userUuid", is(EXPECTED_USER_UUID
                        .toString())));

        verify(commentService, times(1)).findById(EXPECTED_ID);
        verifyNoMoreInteractions(commentService);
    }

    @Test
    @UseDataProvider("getProperComment")
    public void createComment(final Comment comment) throws Exception {
        when(commentService.createComment(any(Comment.class))).thenReturn(comment);
        mockMvc.perform(post(ApiUrl.COMMENTS_PATH)
                .contentType(CONTENT_TYPE)
                .content(new ObjectMapper().writeValueAsString(comment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(EXPECTED_ID.toString())))
                .andExpect(jsonPath("$.petUuid", is(EXPECTED_PET_UUID
                        .toString())))
                .andExpect(jsonPath("$.contents", is(EXPECTED_CONTENTS)))
                .andExpect(jsonPath("$.state", is(EXPECTED_STATE.toString())))
                .andExpect(jsonPath("$.userUuid", is(EXPECTED_USER_UUID
                        .toString())));

        verify(commentService, times(1)).
                createComment(any(Comment.class));
        verifyNoMoreInteractions(commentService);
    }

    @Test
    @UseDataProvider("getProperComment")
    public void updateComment(final Comment comment) throws Exception {
        when(commentService.updateComment(eq(EXPECTED_ID), any(Comment.class)))
                .thenReturn(comment);
        mockMvc.perform(put(String.format("%s/%s", ApiUrl.COMMENTS_PATH, EXPECTED_ID))
                .contentType(CONTENT_TYPE)
                .content(new ObjectMapper().writeValueAsString(comment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(EXPECTED_ID.toString())))
                .andExpect(jsonPath("$.petUuid", is(EXPECTED_PET_UUID
                        .toString())))
                .andExpect(jsonPath("$.contents", is(EXPECTED_CONTENTS)))
                .andExpect(jsonPath("$.userUuid", is(EXPECTED_USER_UUID
                        .toString())));

        verify(commentService, times(1))
                .updateComment(eq(EXPECTED_ID), any(Comment.class));
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void deleteCommentById() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(commentService).deleteComment(id);
        mockMvc.perform(delete(String.format("%s/%s", ApiUrl.COMMENTS_PATH, id)))
                .andExpect(status().isOk());

        verify(commentService, times(1)).deleteComment(id);
        verifyNoMoreInteractions(commentService);
    }
}
