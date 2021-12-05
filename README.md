# Chat&Scheduling App(DroidSoftThird)

Android Application built in kotlin by following Official recommended architecture: MVVM+Repository+ViewModel+Livedata+databinding + CoroutineFlow. This app is supposed to help to create community for studying, having user chat and make schedule with the community. The mockup image and the under-development-screen gif are shown together since it is on progress. Initially, App was created by Java (see Repository: DroidSoftSecond), but I changed it to Kotlin in order to proceed development more efficiently.

このアプリは勉強をするためのコミュニティ作りを助けることを目的としており、チャット・スケジューリング機能などの提供を予定しています。
公式が推奨するMVVMアーキテクチャを参考にし、比較的新しいライブラリ(Coroutine/Flow, LiveData, ViewModel..等)を使い作成を行なっています。
完成途中のため、モックアップ画像(完成予想図)と開発中の画面を併記してあります。
当初はJavaで作成していましたが、効率的な開発を行うため、同レポジトリーではKotlinに言語を変更し開発をしております。
現在、feature/0.1.7-Calendarブランチにて日程スケジューリング機能とマップ機能を実装中です。<br>
※Java版については、レポジトリー名：DroidSoftSecondを参照ください。


## Mockup Screen/モックアップ画像

Mockup Screen is designed by Figma. please check below URL.
モックアップ画像はFigmaで作成しました。下記URLを参照ください。

Figma address: https://www.figma.com/file/eqLAiFS1EeBbuWVD6Dvy8k/DroidThird-SNS-App

## Screen under development/開発中画面
1.ログインからグループ作成まで<br>
![1.ログインからグループ作成まで](https://github.com/tsemb012/DroidSoftThird/blob/master/app/src/main/res/drawable/_0210702_184057.gif)<br>
<br>
２.グループ参加からチャットまで<br>
![２.グループ参加からチャットまで](https://github.com/tsemb012/DroidSoftThird/blob/master/app/src/main/res/drawable/_0210702_184351.gif)

## Features/機能

[ENG]

    Implemented
    ---------------------------------------------------------------------------------------
        Login
        Create User Profile
        Create Study-group
        Display groups by recyclerView
        Display my-groups by recyclerView
        Chat with group-members
            -Send Text, Voice Record, Image, Files


    Under Implementation at feature/0.2-Calendar
    ---------------------------------------------------------------------------------------
        Schdule Study-plan　of groups by Calendar
        Map activity area of groups by GoogleMapAPI

[JPN]

    実装済み
    ---------------------------------------------------------------------------------------
        ログイン機能
        ユーザープロフィール作成
        グループ勉強グループ作成
        グループ一覧表示(リサイクラービュー使用)
        マイページ表示（
            -所属中のグループを一覧で表示
        チャット機能
            -グループ内のメンバーのチャット
            -下記データの送受信が可能
                -ボイスメッセージ
                -ファイルデータ
                -画像データ
                -テキストメッセージ

    現在実装中　　(ブランチfeature/0.2 -Calendarを参照ください。）
    ---------------------------------------------------------------------------------------
        スケジュール
            -グループの勉強プランをカレンダーで管理。

    実装予定　　(ブランチfeature/0.2 -Calendarを参照ください。）
    ---------------------------------------------------------------------------------------
        マップ　
            -グループの活動地域を地図上に表示。
            -GoogleMapAPIを使用。
        ビデオチャット


## Used Tech/使用技術

    USING/使用中

        MVVM
            LiveData
            ViewModel
            DataBinding
        Rx
            Coroutine
            Android-Flow
        Transition
            Navigation
        DI
            Hilt/Dagger
        Auth
            Firebase Authentication
        Image
            Glide
            Firebase CloudStorage
            Stfalcon ImageViewer
        UI
            ViewPager2
            Dialogs
            RecyclerView
            CircleImageView
        DB
            Firebase FireStore
        Media
            MediaPlayer
        Permission
            Dexter


    PLAN TO USE/使用予定
    　
        at feature/0.2-Calendar

            Map
                GoogleMapAPI
            UI
                kizitonwose's CalendarView
                MaterialDialog

        at further branches

            Notification
                AlarmManager
                WorkManager
                Cloud Messaging
            DB
                Firebase CloudFunctions

## Reference/参照

    Book/書籍

        スッキリわかるJava入門 第３版 中山清喬/国本大吾 著
        スッキリわかるJava入門 実践編　第２版 中山清喬 著
        独習 Java 新版　山田祥寛　著
        Java言語で学ぶデザインパターン入門 結城　浩　著
        速習kotlin　山田祥寛　著
        Kotlinスタートブック-新しいAndroidプログラミング-　長澤太郎　著
        Kotlinインアクション Dmistry Jemerov, Svetlana Isakova著
        はじめてのAndroidアプリ開発　第3版　山田祥寛　著
        Androidアプリ設計パターン入門　日高正博, 小西裕介, 藤原聖,吉岡毅, 今井智章 著
        プロに追いつくAndroid開発入門アプリ設計を理解する　Tech Booster編　著
        Master of Dagger Yuki Anzai 著
        独習 Git RickUmali著
        GitHub実践入門　大塚弘記 著　
        Androidテスト全書　白山、外山、平田、菊地、堀江著　(※同著については、現在読み込み中)
                                                                        ...等
    WebSite/Webサイト

        Google Developers Codelabs
            https://codelabs.developers.google.com/
        StackoverFlow
            https://stackoverflow.com/#
        Kotlin Official Reference
            https://dogwood008.github.io/kotlin-web-site-ja/docs/reference/
        Qiita
            https://qiita.com/
        Medium
            https://medium.com/
        Firebase Official Reference
            https://firebase.google.com/docs/android/setup
        Android Developer's Document
            https://developer.android.com/docs

            Also, each libraries official web site and blogs are reffered.
            その他、ライブラリの公式レファレンスやブログを参照。

## Future Plan/今後の課題

      Implementing videoChat./ビデオチャット機能の実装。
      Testing this app./同アプリケーションのテスト実施。
      Applying MaterialDesign./マテリアルデザインの適用。
      MultiModule/マルチモジュール化
      Applying WCAG 2.0 for Design/WCAG2.0に準拠したデザイン
