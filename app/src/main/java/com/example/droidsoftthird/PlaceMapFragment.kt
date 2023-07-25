package com.example.droidsoftthird

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.droidsoftthird.composable.map.place.PlaceMapScreen
import com.example.droidsoftthird.model.domain_model.EditedPlace
import com.google.android.gms.maps.model.LatLng
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            mapViewModel.updateViewState(mapViewModel.viewState.value.copy(currentPoint = currentLocation?.let { LatLng(it.latitude, it.longitude) }))
        }

        return ComposeView(requireContext()).apply {
            setContent {
                PlaceMapScreen(mapViewModel, ::confirmPlace, ::navigateUp)
                mapViewModel.viewState.value.error?.let { Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()}
            }
        }
    }

    private fun confirmPlace(editedPlace: EditedPlace?) {
        if (editedPlace != null) {
            scheduleViewModel.postPlace(editedPlace)
            val action = PlaceMapFragmentDirections.actionMapFragmentToScheduleCreateFragment()//null)
            findNavController().navigate(action)
        }
    }

    fun navigateUp() {
        findNavController().navigateUp()
    }
}
