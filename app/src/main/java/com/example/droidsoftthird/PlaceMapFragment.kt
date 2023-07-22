package com.example.droidsoftthird

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.droidsoftthird.composable.*
import com.example.droidsoftthird.model.domain_model.EditedPlace
import com.example.droidsoftthird.model.domain_model.YolpSimplePlace
import com.example.droidsoftthird.model.presentation_model.LoadState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
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

/*TODO 設計方針
*   ViewModelの中に値を保存
*   Composeの中にViewModelは渡さず、Fragmentでここの値に分割する。
*   ベースのComposeに対して、外部からComposeを入れていくように
*   Composeを入れる際は、mutableの値 と callbackを渡してあげる。
*   rememberは使わないくても良いの？　→　rememberはRecomposeで内部の変数が消えてしまうのを防いでいるが、データベースからくる値は全てViewModelに入っているので、
*   Fragment側から渡す値に関しては、不要。
*   uiStateはComposeから出さない。LoadStateはViewModel内で分解し、Stateでラップするように
*   2つComposeをまたぐ場合は？　→　この場合は上部に新しいComposewo作りラップするようにする。
*/

//TODO サーバーから降ってくるデータをViewModelに保持するようにする。
//TODO uiStateをjetpackComposeの内部に止める構造にする。
//TODO 不要な変数を削除してくようにする。
//TODO 公式にuiStateのベストプラクティスみたいなのがあるので、それに合わせていく。

@AndroidEntryPoint
class PlaceMapFragment: Fragment() {

    private val mapViewModel: PlaceMapViewModel by viewModels()
    private val scheduleViewModel:ScheduleCreateViewModel by navGraphViewModels(R.id.schedule_graph)

    val tokyo = LatLng(35.681236, 139.767125)
    val defaultCameraPosition = CameraPosition.fromLatLngZoom(tokyo, 11f)//これを現在地に変更する。

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { //TODO 要リファクタリング

        return ComposeView(requireContext()).apply {
            setContent {
                var isMapLoaded by remember { mutableStateOf(false) }
                BottomModal(
                        placeDetailLoadState = mapViewModel.placeDetailLoadState,
                        onConfirm = { confirmPlace(it) }
                ) {
                    Column {
                        if (mapViewModel.isLoading) LinearProgressIndicator(Modifier.fillMaxWidth())
                        if (mapViewModel.isError) {
                            if (mapViewModel.placeDetailLoadState.value is LoadState.Error) Toast.makeText(requireContext(), mapViewModel.placeDetailLoadState.value.getErrorOrNull().toString(), Toast.LENGTH_SHORT).show()
                            if (mapViewModel.placesLoadState.value is LoadState.Error) Toast.makeText(requireContext(), mapViewModel.placesLoadState.value.getErrorOrNull().toString(), Toast.LENGTH_SHORT).show()
                        }
                        Box {
                            val cameraPositionState = rememberCameraPositionState { position = defaultCameraPosition }
                            GoogleMap(
                                cameraPositionState = cameraPositionState,
                                modifier = Modifier,
                                properties = MapProperties(mapType = MapType.NORMAL),
                                uiSettings = MapUiSettings(compassEnabled = false),
                                onMapLoaded = { isMapLoaded = true },
                                onPOIClick = { },
                                content = function(cameraPositionState, mapViewModel.centerPoint, mapViewModel.radius),
                            )
                            Column {
                                Row(modifier = Modifier.height(56.dp).padding(top = 16.dp)) {
                                    SearchBox(mapViewModel.query) { mapViewModel.searchByText() }
                                    DropDown(mapViewModel.selections, mapViewModel.selectedType)
                                }
                                ChipGroup(mapViewModel.selections) { mapViewModel.searchByPoi() }
                            }
                        }
                    }
                }
                CircleProgressBar(isMapLoaded)
            }
        }
    }

    private fun confirmPlace(editedPlaceDetail: EditedPlace?) {

        if (editedPlaceDetail != null) {
            val action = PlaceMapFragmentDirections.actionMapFragmentToScheduleCreateFragment()//null)
            findNavController().navigate(action)
            scheduleViewModel.postPlace(editedPlaceDetail)
        }
    }



    @Composable
    private fun function(
        cameraPositionState: CameraPositionState,
        centerPoint: MutableState<LatLng>,
        radius: MutableState<Int>,


    ): @Composable () -> Unit =
        {
            val updateCameraPosition: (northEast: LatLng, southWest: LatLng) -> Unit = { _, _ -> }
            if (!cameraPositionState.isMoving) {//カメラの動きが止まった時のデータをViewModelにあげるようにする。
                cameraPositionState.projection?.visibleRegion?.latLngBounds?.let {
                    updateCameraPosition(it.northeast, it.southwest)
                    cameraPositionState
                    centerPoint.value = it.center
                    radius.value = distanceInMeters(
                            it.center.latitude,
                            it.center.longitude,
                            it.center.latitude,
                            it.northeast.longitude
                    ).toInt()
                    mapViewModel.updateViewPoint(it.northeast, it.southwest)
                }
            }

            if (mapViewModel.placesLoadState.value is LoadState.Loaded<*> ) {
                mapViewModel.placesLoadState.value.getValueOrNull<List<YolpSimplePlace>>()?.forEach {
                    Marker(
                            state = MarkerState(position = LatLng(it.location.lat, it.location.lng)),
                            tag = it.id,
                            title = it.name,
                            onClick = { marker ->
                                mapViewModel.fetchPlaceDetail((marker.tag.toString()))
                                true
                            }
                    )
                }
                //viewModel.placeDetailLoadState.value = LoadState.Modified(Pair(viewModel.placeDetailLoadState.value.getValueOrNull() , null))
            }
        }

    private fun distanceInMeters(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val earthRadius = 6371.0 // 地球の半径
        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
            Math.sin(dLng / 2) * Math.sin(dLng / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return (earthRadius * c * 1000.0) * 0.8
    }
}
