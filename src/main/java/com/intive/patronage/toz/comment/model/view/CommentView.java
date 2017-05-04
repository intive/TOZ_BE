package com.intive.patronage.toz.comment.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.intive.patronage.toz.base.model.IdentifiableView;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CommentView extends IdentifiableView {
    @NotNull
    @NotEmpty
    private String contents;
    private UUID userUuid;

    @Valid
    @NotNull
    private UUID petUuid;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long created;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long lastModified;
}
