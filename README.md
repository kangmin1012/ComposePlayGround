# ComposePlayGround
컴포즈와 관련해서 이것저것 테스트 해보는 샘플 프로젝트 입니다

## 특징
- 컴포즈를 쓰면서 다양한 상황에서 쓰일 법한 UI나 특별하게 구성되는 UI를 이것저것 실험해보는 프로젝트입니다.
- 컴포즈 UI를 주로 다루고 연습용 프로젝트이기 때문에 따로 Clean Architecure등을 통해 구조를 분리하지는 않습니다.
- 구현에 어려움을 겪는 UI가 생길 때 해당 프로젝트에서 테스트하고 적용해봅니다.
- 각각 테스트 내용은 Activity로 분리해 이동하여 확인가능하도록 구성했습니다.

## 히스토리
- 2024년 9월 : Target SDK 34를 대상으로하는 프로젝트에 미디어 권한 `READ_MEDIA_VISUAL_USER_SELECTED`를 테스트했습니다.
  - [MediaPermissionActivity.kt](https://github.com/kangmin1012/ComposePlayGround/blob/master/app/src/main/java/compose/play/ground/permission/MediaPermissionActivity.kt)
- 2024년 9월 : 애니메이션으로 차오르는 Progress, Dash 형태의 Progress를 Material3 디자인이 아닌 일반적인 Progress 느낌을 표현하기 위해 테스트했습니다.
  - [ProgressActivity.kt](https://github.com/kangmin1012/ComposePlayGround/blob/master/app/src/main/java/compose/play/ground/progress/ProgressActivity.kt)
- 2024년 9월 : LazyColumn을 이용하여 가장 상단 아이템이 화면에서 사라졌을 때 상단 영역에 하나의 고정적인 아이템을 표시해주는 레이아웃을 표현하기 위해 테스트했습니다.
  - [CoordinateActivity](https://github.com/kangmin1012/ComposePlayGround/blob/master/app/src/main/java/compose/play/ground/coordinate/CoordinateActivity.kt) 
