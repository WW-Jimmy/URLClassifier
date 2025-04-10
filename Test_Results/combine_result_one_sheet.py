import os
import glob
import pandas as pd
from datetime import datetime

def combine_results_to_one_sheet():
    # 스크립트가 있는 위치를 가져옵니다
    results_dir = os.path.dirname(os.path.abspath(__file__))
    
    # 출력 디렉토리를 스크립트와 같은 폴더로 설정합니다
    output_dir = results_dir
    
    # 모든 결과 파일을 찾습니다
    result_files = glob.glob(os.path.join(results_dir, "*_Test_result.txt"))
    
    if not result_files:
        print("결과 파일을 찾을 수 없습니다:", results_dir)
        return
    
    # 파일 경로에서 국가 코드 추출 후 알파벳 순으로 정렬
    country_files = []
    for file_path in result_files:
        filename = os.path.basename(file_path)
        country_code = filename.replace("_Test_result.txt", "")
        country_files.append((country_code, file_path))
    
    # 국가 코드 알파벳 순으로 정렬
    country_files.sort(key=lambda x: x[0])
    
    print(f"{len(result_files)}개의 결과 파일을 찾았습니다 (알파벳 순으로 처리됨)")
    
    # 모든 데이터를 저장할 빈 DataFrame 생성
    all_data = pd.DataFrame()
    
    # 정렬된 국가 파일 목록을 처리
    for country_code, file_path in country_files:
        print(f"처리 중: {country_code}")
        
        try:
            # CSV 파일 읽기
            df = pd.read_csv(file_path)
            
            # 데이터프레임에 추가 (원래 순서 유지)
            all_data = pd.concat([all_data, df], ignore_index=True)
            
            print(f"{country_code}에 대해 {len(df)}개 행이 추가되었습니다")
        except Exception as e:
            print(f"{file_path} 처리 중 오류 발생: {str(e)}")
    
    # 짧은 날짜 형식(YYMMDD)으로 타임스탬프 생성
    timestamp = datetime.now().strftime("%y%m%d")
    output_file = os.path.join(output_dir, f"Overall_Test_Results_OneSheet_{timestamp}.xlsx")
    
    # 엑셀 파일로 저장 (단일 시트)
    all_data.to_excel(output_file, sheet_name='All_Results', index=False)
    
    print(f"총 {len(all_data)}개 행이 통합되었습니다")
    print(f"결과가 다음 위치에 저장되었습니다: {output_file}")

if __name__ == "__main__":
    try:
        combine_results_to_one_sheet()
        print("프로세스가 성공적으로 완료되었습니다!")
    except Exception as e:
        print(f"오류가 발생했습니다: {str(e)}")
    
    # 콘솔 창을 열어둡니다
    input("종료하려면 Enter 키를 누르세요...")