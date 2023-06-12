package nextstep.sessions.domain;

import nextstep.users.domain.NsUser;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SessionStudents {
    private Set<NsUser> students;
    private int maximumCapacity;

    public SessionStudents(Set<NsUser> users, int maximumCapacity) {
        this.students = users;
        this.maximumCapacity = maximumCapacity;
    }

    public SessionStudents(int maximumCapacity) {
        this(new HashSet<>(), maximumCapacity);
    }

    private boolean canRecruitStudent(int maximumCapacity) {
        return maximumCapacity <= students.size();
    }

    public boolean contains(Long nsUserId) {
        return students.stream().filter(value -> value.getId().equals(nsUserId)).findFirst().isPresent();
    }

    public void enrollStudent(NsUser nsUser) {
        if (contains(nsUser.getId())) {
            throw new IllegalArgumentException("이미 등록된 학생입니다");
        }

        if (canRecruitStudent(maximumCapacity)) {
            throw new IllegalArgumentException("정원수를 초과했습니다");
        }

        students.add(nsUser);
    }

    public int getCurrentStudentCount() {
        return students.size();
    }

    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    @Override
    public String toString() {
        return "SessionStudents{" +
                "students=" + students +
                ", maximumCapacity=" + maximumCapacity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionStudents that = (SessionStudents) o;
        return maximumCapacity == that.maximumCapacity && Objects.equals(students, that.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(students, maximumCapacity);
    }
}