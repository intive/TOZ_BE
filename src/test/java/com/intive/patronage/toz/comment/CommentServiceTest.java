package com.intive.patronage.toz.comment;

import com.intive.patronage.toz.comment.model.db.Comment;
import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.tokens.model.UserContext;
import com.intive.patronage.toz.users.UserRepository;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(DataProviderRunner.class)
public class CommentServiceTest {
    private static final UUID EXPECTED_ID = UUID.randomUUID();
    private static final UUID EXPECTED_PET_UUID = UUID.randomUUID();
    private static final UUID DEFAULT_PET_UUID = null;
    private static final String EXPECTED_CONTENTS = "Very nice dog!";
    private static final UUID EXPECTED_USER_UUID = UUID.randomUUID();
    private static final Boolean DEFAULT_SHORTENED = false;
    private static final Boolean DEFAULT_ORDERED = false;
    private static final UserContext USER_CONTEXT = new UserContext(EXPECTED_USER_UUID,
            null, null);
    private Comment comment;
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        commentService = new CommentService(commentRepository, userRepository);
        comment = new Comment();
        comment.setPetUuid(EXPECTED_PET_UUID);
        comment.setContents(EXPECTED_CONTENTS);
        comment.setUserUuid(EXPECTED_USER_UUID);
    }

    @DataProvider
    public static Object[] getProperComment() {
        Comment commentDb = new Comment();
        commentDb.setId(EXPECTED_ID);
        commentDb.setPetUuid(EXPECTED_PET_UUID);
        commentDb.setContents(EXPECTED_CONTENTS);
        commentDb.setUserUuid(EXPECTED_USER_UUID);
        return new Comment[]{commentDb};
    }

    @Test
    public void findAllComments() throws Exception {
        when(commentRepository.findAll()).thenReturn(Collections.emptyList());

        List<Comment> commentList = commentService.findAllComments(DEFAULT_PET_UUID,
                DEFAULT_SHORTENED, DEFAULT_ORDERED);
        assertTrue(commentList.isEmpty());
    }

    @Test
    public void findAllCommentsByPetUuid() throws Exception {
        when(commentRepository.findByPetUuid(EXPECTED_PET_UUID)).thenReturn(Collections
                .emptyList());

        List<Comment> commentList = commentService
                .findAllComments(EXPECTED_PET_UUID, DEFAULT_SHORTENED, DEFAULT_ORDERED);
        assertTrue(commentList.isEmpty());
    }

    @Test
    @UseDataProvider("getProperComment")
    public void findById(final Comment commentDb) throws Exception {
        when(commentRepository.exists(EXPECTED_ID)).thenReturn(true);
        when(commentRepository.findOne(EXPECTED_ID)).thenReturn(commentDb);

        Comment comment = commentService.findById(EXPECTED_ID);
        assertEquals(EXPECTED_CONTENTS, comment.getContents());
        assertEquals(EXPECTED_PET_UUID, comment.getPetUuid());

        verify(commentRepository, times(1))
                .exists(eq(EXPECTED_ID));
        verify(commentRepository, times(1))
                .findOne(eq(EXPECTED_ID));
        verifyNoMoreInteractions(commentRepository);
    }

    @Test(expected = NotFoundException.class)
    public void findByIdNotFoundException() throws Exception {
        when(commentRepository.exists(EXPECTED_ID)).thenReturn(false);
        commentService.findById(EXPECTED_ID);

        verify(commentRepository, times(1))
                .exists(eq(EXPECTED_ID));
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    @UseDataProvider("getProperComment")
    public void createComment(final Comment commentDb) throws Exception {
        when(commentRepository.save(any(Comment.class))).thenReturn(commentDb);

        Comment comment = commentService.createComment(this.comment);
        assertEquals(EXPECTED_CONTENTS, comment.getContents());
        assertEquals(EXPECTED_PET_UUID, comment.getPetUuid());

        verify(commentRepository, times(1))
                .save(any(Comment.class));
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    public void deleteComment() throws Exception {
        SecurityContextHolder.setContext(securityContext);
        when(commentRepository.findOne(EXPECTED_ID)).thenReturn(this.comment);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(USER_CONTEXT);
        when(commentRepository.exists(any(UUID.class))).thenReturn(true);
        when(userRepository.exists(any(UUID.class))).thenReturn(true);

        commentService.deleteComment(EXPECTED_ID);
        SecurityContextHolder.clearContext();

        verify(commentRepository, times(1))
                .exists(eq(EXPECTED_ID));
        verify(commentRepository, times(1))
                .delete(eq(EXPECTED_ID));
        verify(commentRepository, times(1))
                .findOne(eq(EXPECTED_ID));
        verifyNoMoreInteractions(commentRepository);
    }

    @Test(expected = NotFoundException.class)
    public void deleteCommentNotFoundException() throws Exception {
        when(commentRepository.exists(EXPECTED_ID)).thenReturn(false);

        commentService.deleteComment(EXPECTED_ID);

        verify(commentRepository, times(1))
                .exists(eq(EXPECTED_ID));
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    @UseDataProvider("getProperComment")
    public void updateComment(final Comment commentDb) throws Exception {
        SecurityContextHolder.setContext(securityContext);
        when(commentRepository.exists(EXPECTED_ID)).thenReturn(true);
        when(commentRepository.save(any(Comment.class))).thenReturn(commentDb);
        when(commentRepository.findOne(EXPECTED_ID)).thenReturn(commentDb);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(USER_CONTEXT);
        when(userRepository.exists(any(UUID.class))).thenReturn(true);

        Comment comment = commentService.updateComment(EXPECTED_ID, this.comment);
        assertEquals(EXPECTED_CONTENTS, comment.getContents());
        assertEquals(EXPECTED_PET_UUID, comment.getPetUuid());
        SecurityContextHolder.clearContext();

        verify(commentRepository, times(1))
                .exists(eq(EXPECTED_ID));
        verify(commentRepository, times(1))
                .save(any(Comment.class));
        verify(commentRepository, times(1))
                .findOne(eq(EXPECTED_ID));
        verifyNoMoreInteractions(commentRepository);
    }

    @Test(expected = NotFoundException.class)
    public void updateCommentNotFoundException() throws Exception {
        when(commentRepository.exists(EXPECTED_ID)).thenReturn(false);

        commentService.updateComment(EXPECTED_ID, comment);

        verify(commentRepository, times(1))
                .exists(eq(EXPECTED_ID));
        verifyNoMoreInteractions(commentRepository);
    }
}
