package com.intive.patronage.toz.comment.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.intive.patronage.toz.base.model.IdentifiableView;
import com.intive.patronage.toz.comment.model.db.Comment;
import com.intive.patronage.toz.util.validation.EnumValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@ApiModel(value = "Comment")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CommentView extends IdentifiableView {
    @ApiModelProperty(example = "Bardzo fajny piesek!", position = 1,
            required = true)
    @NotNull
    @NotEmpty
    @Size(min = 1, max = 500)
    private String contents;

    @ApiModelProperty(example = "c5296892-347f-4b2e-b1c6-6feaff971f67", position = 2)
    private UUID userUuid;

    @ApiModelProperty(example = "bd7ee39d-4472-4a75-a141-7929d2ffd7dc", position = 3,
            required = true)
    @Valid
    @NotNull
    private UUID petUuid;

    @ApiModelProperty(example = "ACTIVE", allowableValues = "ACTIVE, DELETED",
            position = 4)
    @EnumValidate(enumClass = Comment.State.class)
    private String state;

    @ApiModelProperty(example = "1222333444555", position = 5)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long created;

    @ApiModelProperty(example = "1222333444555", position = 6)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long lastModified;
}
