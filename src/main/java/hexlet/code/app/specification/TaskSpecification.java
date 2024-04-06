package hexlet.code.app.specification;

import hexlet.code.app.dto.task.TaskParamsDTO;
import hexlet.code.app.model.Task;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {
    public Specification<Task> build(TaskParamsDTO params) {
        return withNameCont(params.getTitleCont())
                .and(withAssigneeId(params.getAssigneeId()))
                .and(withSlug(params.getStatus()))
                .and(withLabelId(params.getLabelId()));
    }

    private Specification<Task> withNameCont(String content) {
        return (root, query, cb) -> content == null
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("name")), "%" + content.toLowerCase() + "%");
    }

    private Specification<Task> withAssigneeId(Long assigneeId) {
        return (root, query, cb) -> assigneeId == null
                ? cb.conjunction()
                : cb.equal(root.get("assignee").get("id"), assigneeId);
    }

    private Specification<Task> withSlug(String status) {
        return (root, query, cb) -> status == null
                ? cb.conjunction()
                : cb.equal(root.join("taskStatus", JoinType.INNER).get("slug"), status);
    }

    private Specification<Task> withLabelId(Long labelId) {
        return (root, query, cb) -> labelId == null
                ? cb.conjunction()
                : cb.equal(root.join("labels", JoinType.INNER).get("id"), labelId);
    }

}
