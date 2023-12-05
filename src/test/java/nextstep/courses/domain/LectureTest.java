package nextstep.courses.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import nextstep.users.domain.NsUser;
import nextstep.users.domain.NsUserTest;
import nextstep.users.domain.Payment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LectureTest {

  @Test
  @DisplayName("무료 강의 생성")
  public void free_lecture_test() {
    // given
    LocalDateTime startDate = LocalDateTime.of(2023, 4, 3, 11, 30);
    LocalDateTime endDate = LocalDateTime.of(2023, 6, 3, 11, 30);
    CoverImage coverImage = CoverImage.defaultOf("file.png", 2000L, 900, 600);
    // when
    Lecture lecture = Lecture.freeOf("free lecture", coverImage, LectureType.FREE, startDate,
        endDate);
    boolean result = lecture.isFree();
    // then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("유료 강의 인원 제한 테스트")
  public void paid_lecture_person_limit_test() {
    // given
    LocalDateTime startDate = LocalDateTime.of(2023, 4, 3, 11, 30);
    LocalDateTime endDate = LocalDateTime.of(2023, 6, 3, 11, 30);
    CoverImage coverImage = CoverImage.defaultOf("file.png", 2000L, 900, 600);
    int maxStudent = 0;

    // when
    Lecture lecture = Lecture.paidOf("paid lecture", coverImage, BigDecimal.ONE, LectureType.PAID,
        startDate, endDate, maxStudent);

    // then
    assertThrows(IllegalArgumentException.class
        , () -> lecture.enrolment(NsUserTest.SANJIGI));
  }

  @Test
  @DisplayName("강의 수강 신청 테스트")
  public void lecture_enrolment() {
    // given
    LocalDateTime startDate = LocalDateTime.of(2023, 4, 3, 11, 30);
    LocalDateTime endDate = LocalDateTime.of(2023, 6, 3, 11, 30);
    CoverImage coverImage = CoverImage.defaultOf("file.png", 2000L, 900, 600);
    int maxStudent = 1;
    Lecture lecture = Lecture.paidOf("paid lecture", coverImage, BigDecimal.ONE, LectureType.PAID,
        startDate, endDate, maxStudent);
    NsUser javajigi = NsUserTest.JAVAJIGI;
    javajigi.payment(new Payment(BigDecimal.ONE));

    // when
    Lecture newLecture = lecture.start();
    newLecture.enrolment(javajigi);

    // then
    assertThat(lecture.numberOfStudent()).isEqualTo(1);
  }

  @Test
  @DisplayName("모집중이지 않은 강의 수강신청 테스트")
  public void lecture_enrolment_status() {
    // given
    LocalDateTime startDate = LocalDateTime.of(2023, 4, 3, 11, 30);
    LocalDateTime endDate = LocalDateTime.of(2023, 6, 3, 11, 30);
    CoverImage coverImage = CoverImage.defaultOf("file.png", 2000L, 900, 600);
    int maxStudent = 1;

    // when
    Lecture lecture = Lecture.paidOf("paid lecture", coverImage, BigDecimal.ONE, LectureType.PAID,
        startDate, endDate, maxStudent);
    // then
    assertThrows(IllegalArgumentException.class
        , () -> lecture.enrolment(NsUserTest.SANJIGI));
  }

  @Test
  @DisplayName("수강신청에서 결제가 다를 경우")
  public void lecture_enrolment_different_fail() {
    // given
    LocalDateTime startDate = LocalDateTime.of(2023, 4, 3, 11, 30);
    LocalDateTime endDate = LocalDateTime.of(2023, 6, 3, 11, 30);
    CoverImage coverImage = CoverImage.defaultOf("file.png", 2000L, 900, 600);
    int maxStudent = 1;
    Lecture lecture = Lecture.paidOf("paid lecture", coverImage, BigDecimal.ONE, LectureType.PAID,
        startDate, endDate, maxStudent);
    NsUser javajigi = NsUserTest.JAVAJIGI;
    javajigi.payment(new Payment(BigDecimal.TEN));
    Lecture startLecture = lecture.start();

    // when

    // then
    assertThrows(IllegalArgumentException.class
        , () -> startLecture.enrolment(javajigi));
  }
}
