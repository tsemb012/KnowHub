package com.example.droidsoftthird

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.composable.ChipGroup
import com.example.droidsoftthird.composable.DropDown
import com.example.droidsoftthird.composable.SearchBox
import com.example.droidsoftthird.composable.map.GoogleMapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import dagger.hilt.android.AndroidEntryPoint

//DONE 図にまとめるOK　→　マップ入力画面UIの情報収集を行うOK　→　UIを確定させるOK　→　データのやりとりをざっくり検討するOK　→　TODOを設置して全体像を掴む　→　
//TODO ComposableのPreviewを用意する。 →　
//TODO UI側にTODOを埋め込みMapの実現性を確認する。　→　データクラスを作成　→　データの流れの骨子を埋めていく　→　使用するAPIを確定させる　→　引数を確定　→　画面を作り始める。
//TODO APIから取得したデータをマーカーとして表示させる。　→　登録画面に表示させる。(表示・入力を実施する。)　→　登録ボタンを押したら全画面にデータを渡すようにする。
//TODO 厳密には登録ではないかも　→　
//TODO マーカーの設置方法を確認する。　→　
//TODO データクラスはどのように設定したか？
//TODO デフォルトの位置を現在地にする。
//TODO 登録画面を作る。　→　詳細画面　と　登録画面を同一にする。
//TODO 登録画面の項目と詳細なレイアウトを考える。　→　表示する：場所名・住所（県・市）・メモ・Googleのリンク・HPのリンク　　表示しない：緯度・経度
//TODO 土台はどうやって表示するの？　→
//TODO マーカーの設置方法・タップされた時のコールバック（ウィンドウ・ダイアログを出現させる。）・
//TODO マーカーの出現サイクルは？　→　検索マーカー　と　現在地マーカー　を共存させる。　→　検索するたびに検索マーカーを消すようにする。
//TODO 必要な情報が出揃ったところで、情報の整合性を取る必要がある。　→　情報の経路をシュミレートする必要がある。
//TODO ピンがあるところをタッチすると詳細が入力された状態、ピンがないところを長押しすると空欄の情報登録画面が出てくる。

//TODO ネットがつながったらComposableのpreviewを用意するようにする。

@AndroidEntryPoint
class MapFragment: Fragment() {

    private val viewModel: MapViewModel by viewModels()
    val tokyo = LatLng(35.681236, 139.767125)
    val defaultCameraPosition = CameraPosition.fromLatLngZoom(tokyo, 11f)//これを現在地に変更する。

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                var isMapLoaded by remember { mutableStateOf(false) }
                val cameraPositionState = rememberCameraPositionState { position = defaultCameraPosition }
                GoogleMapView(
                        modifier = Modifier,
                        cameraPositionState = cameraPositionState,
                        onMapLoaded = { isMapLoaded = true },
                        updateCameraPosition = { northEast, southWest -> viewModel.updateViewPoint(northEast, southWest) },
                        placesLoadState = viewModel.placesLoadState,
                        currentPoint = viewModel.centerPoint,
                        currentRadius = viewModel.radius,
                        onMarkerClick = { placeId -> viewModel.fetchPlaceDetail(placeId) },
                        composableSearchBox = { SearchBox(viewModel.query) { viewModel.searchByText() } },
                        composableDropDown = { DropDown(viewModel.selections, viewModel.selectedType) },
                        composableChipGroup = { ChipGroup(viewModel.selections.value) { viewModel.searchByPoi() } },
                )
                if (!isMapLoaded) {
                    AnimatedVisibility(
                        modifier = Modifier.fillMaxSize(),
                        visible = !isMapLoaded,
                        enter = EnterTransition.None,
                        exit = fadeOut()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .background(MaterialTheme.colors.background)
                                .wrapContentSize()
                        )
                    }
                }
            }
        }
    }
}
