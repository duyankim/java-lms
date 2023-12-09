package nextstep.session.domain;

import nextstep.common.domain.BaseDomain;
import nextstep.users.domain.NsUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public abstract class Session extends BaseDomain {
    private static final SessionStatus DEFAULT_SESSION_STATUS = SessionStatus.PREPARING;

    private Long creatorId;

    private LocalDate startDate;

    private LocalDate endDate;

    private SessionImage sessionImage;

    protected SessionStatus sessionStatus;
    protected SessionStudents students = new SessionStudents();

    public Session(Long creatorId, LocalDate startDate, LocalDate endDate, SessionImage sessionImage) {
        this(0L, LocalDateTime.now(), null, creatorId, startDate, endDate, sessionImage, DEFAULT_SESSION_STATUS);
    }

    public Session(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, Long creatorId, LocalDate startDate, LocalDate endDate, SessionImage sessionImage, SessionStatus sessionStatus) {
        super(id, createdAt, updatedAt);
        this.creatorId = creatorId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sessionImage = sessionImage;
        this.sessionStatus = sessionStatus;
    }

    public void enroll(NsUser user) {
        validateStatus();
        validateCommonEnroll(user);
        students.add(user);
    }

    private void validateStatus() {
        if (sessionStatus != SessionStatus.RECRUITING) {
            throw new IllegalStateException("모집중인 강의만 신청 가능합니다.");
        }
    }

    protected void validateCommonEnroll(NsUser nsUser) {
    }

    public List<NsUser> getStudents() {
        return students.getStudents();
    }

    public void changeStatus(SessionStatus status) {
        sessionStatus = status;
    }
}
