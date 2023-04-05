# Project_android
Андроид проект Travel


Это нейросеть

Чтобы сделать анимацию вращения карты пальцами, как в Яндекс Картах или osmdroid, вы можете использовать класс GestureDetector для обнаружения жеста вращения, а затем анимировать вращение с помощью класса RotateAnimation. Вот пример фрагмента кода для достижения этой цели:

```
открытый класс MapActivity расширяет AppCompatActivity, реализует OnMapReadyCallback, GestureDetector.OnGestureListener {

    приватный MapView mapView;
    приватный GestureDetector
    приватный float currentBearing = 0;

    @Override
    protected void onCreate (Bundle saveInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_map);

        // Инициализируем вид карты
        mapView = findViewById(R.id.map_view);
        mapView.setBuiltInZoomControls(false);
        mapView.setMultiTouchControls(true);

        // Устанавливаем обратный вызов готовности карты
        mapView.getMapAsync (это);

        // Инициализируем детектор жестов
        жестДетектор = новый Детектор Жестов (это, это);
    }

    @Override
    общественное логическое значение onTouchEvent (событие MotionEvent) {
        // Передаем событие касания детектору жестов
        жестДетектор.onTouchEvent(событие);
        вернуть super.onTouchEvent (событие);
    }

    @Override
    общественное логическое значение onDown (MotionEvent e) {
        вернуть истину;
    }

    @Override
    общественное логическое значение onScroll (MotionEvent e1, MotionEvent e2, расстояние с плавающей запятой X, расстояние с плавающей запятой Y) {
        // Рассчитываем угол поворота
        float angle = (float) Math.toDegrees(Math.atan2(e2.getY() - e1.getY(), e2.getX() - e1.getX()));

        // Рассчитываем дельту поворота
        плавающая дельта = угол - текущий подшипник;

        // Анимируем вращение
        Анимация RotateAnimation = новая RotateAnimation (текущий подшипник, угол, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        анимация.setDuration(200);
        анимация.setFillAfter (истина);
        mapView.startAnimation(анимация);

        // Обновить текущий пеленг
        текущий подшипник = угол;

        вернуть истину;
    }

    // Другие методы интерфейса GestureDetector.OnGestureListener
    // ...

    @Override
    public void onMapReady (MapboxMap mapboxMap) {
        // Настраиваем вид карты
        mapboxMap.setStyle(Style.MAPBOX_STREETS);
        mapboxMap.setCameraPosition (новый CameraPosition.Builder()
                .target (новый LatLng (51,5074, 0,1278))
                .зум(10)
                .подшипник (текущий подшипник)
                .строить());
    }
}
```

В этом примере мы сначала инициализируем GestureDetector в методе onCreate и устанавливаем его прослушиватель касаний в методе onTouchEvent. В методе onScroll мы вычисляем угол поворота и дельту, а затем анимируем вращение с помощью класса RotateAnimation. Наконец, мы обновляем текущий азимут и передаем его в `CameraPosition`, когда карта готов
