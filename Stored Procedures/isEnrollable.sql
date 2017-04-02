create or replace PROCEDURE isEnrollable(studentId IN VARCHAR2,courseId IN VARCHAR2,instrId IN VARCHAR2,  grade IN VARCHAR2,
sessionId IN VARCHAR2,status IN VARCHAR2,credits IN Integer, success OUT Integer, issue OUT VARCHAR2)
AS
stu_gpa Float;
req_gpa Float;
spl_perm VARCHAR2(30);
prereq_check Number;
BEGIN
  --gpa requirement check
  SELECT gpa into stu_gpa from STUDENT where student_id like studentId;
  SELECT min_gpa into req_gpa from COURSES where course_id like courseId;
  IF stu_gpa<req_gpa then
  issue := 'Student GPA requirement is not satisfied for this course';
  success := 1;
  return;
  else
  success := 0;
  End IF;
  --gpa requirement check - end
  
  
  SELECT SPECIAL_PERM into spl_perm from COURSES where course_id like courseId;
  --dbms_output.put_line(spl_perm);
  IF spl_perm like 'None' Then
    success := 0;
  elsif spl_perm like 'Prereq' then
  SELECT count(*) into prereq_check from PREREQ where course_id like courseId and prereq_cid not in 
  (select course_id from enrollment where student_id like studentId);
    if prereq_check>0 then
      issue := 'Enrollment Error : Student has not taken all prerequisite courses';
      success :=1;
      return;
    else
    success := 0;
    end if;
  
  elsif spl_perm like 'SPP' then
  --special permission row added to pending_permission
  insert into PENDING_PERMISSIONS("COURSE_ID","STUDENT_ID") values(courseId,studentId);
  success := 2;
  issue := 'Special permission request sent to department';
  
  elsif spl_perm like 'SPPERM' then
  --dbms_output.put_line('No spl permission required');
  SELECT count(*) into prereq_check from PREREQ where course_id like courseId and prereq_cid not in 
  (select course_id from enrollment where student_id like studentId);
  if prereq_check>0 then
      issue := 'Enrollment Error : Student has not taken all prerequisite courses';
      success :=1;
      return;
    end if;
    
  insert into PENDING_PERMISSIONS("COURSE_ID","STUDENT_ID") values(courseId,studentId);
  success := 2;
  issue := 'Special permission request sent to department';
  
  END IF;
  
END isEnrollable;