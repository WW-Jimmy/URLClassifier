@echo off
chcp 65001 > nul
echo 테스트 결과 파일을 단일 시트로 병합을 시작합니다...
python combine_result_one_sheet.py
echo 프로세스가 완료되었습니다. 아무 키나 눌러 종료하세요.
pause > nul