# GridColorPicker

<p align="center"> <img align="center" src="https://raw.githubusercontent.com/panshen/GridColorPicker/main/app/src/main/res/drawable/logo.png" width="80"/></p>
<p align="center"> <strong>A flexible and beautiful color picker for Android</strong> </p>
<br>
<br>
<p align="center"> <img align="center" src="https://github.com/panshen/GridColorPicker/blob/main/screencapture/view_en.jpg" width="350"/></p>

USAGE
-----



### XML
Use xml to declare a GridColorPicker, every gcp_xxx property is optional.
```xml 
<com.panshen.gridcolorpicker.GridColorPicker
    android:id="@+id/colorPicker"
    android:layout_width="300dp"
    android:layout_height="wrap_content"
    android:layout_gravity="center"
    app:gcp_row="10"
    app:gcp_colorScheme="@array/colors"
    app:gcp_selectorColor="@color/white"
    app:gcp_showAlphaView="true"
    app:gcp_showAlphaViewLabel="true"
    app:gcp_alphaViewLabelColor="@color/black"
    app:gcp_alphaViewLabelText="@string/opacity"
    app:gcp_cardColor="@color/white"
    app:gcp_drawCard="true" />
```
Properties:

| Propertie              |                   Description                |         Default                                               |
| ---------------------- | ---------------------------------------------|---------------------------------------------------------------|
| gcp_row                | The number of rows in the grid, minimal is 2.|                              10                               |
| gcp_colorScheme        | The color scheme of the grid, minimal number of colors is 2.|         #029FD6...#76BC40                      |
| gcp_selectorColor      | The color resource of the selector.          |                            #FFFFFF                            |
| gcp_showAlphaView      | Whether to show the AlphaView.               |                              true                             |
| gcp_showAlphaViewLabel | Whether to show label of the AlphaView. If showAlphaView is false, this property will be ignored. |   true   |
| gcp_alphaViewLabelColor| The color resource of the label.             |                            #000000                            |
| gcp_alphaViewLabelText | The text of the AlphaView label.             |                            Opacity                            |
| gcp_drawCard           | Whether to draw card background of the GridColorPicker. |                   true                             |
| gcp_cardColor          | The color resource of the card.              |                            #FDFDFD                            |

### Create GridColorPicker
For example, create a GridColorPicker that has 5 rows of colors and the AlphaView is hidden:

<p align="center"> <img align="center" src="https://github.com/panshen/GridColorPicker/blob/main/screencapture/row5.jpg" width="350"/></p>

#### Kotlin
```kotlin
val gridColorPicker = colorPickerView {
      row = 5
      showAlphaView = false
}.build(this)
```

#### JAVA
```java
  GridColorPicker gridColorPicker = new ColorPickerViewBuilder(this)
          .setRow(5)
          .alphaViewEnable(false)
          .build();
```

### Create GridColorPickerDialog
For convenience, you are able to create a Dialog which holding a GridColorPicker inside:
<p align="center"> <img align="center" src="https://github.com/panshen/GridColorPicker/blob/main/screencapture/dialogRow5_en.jpg" width="350"/></p>

#### Kotlin
```kotlin
val dialog = colorPickerDialog {
      cancelable = true
      positiveButtonText = resources.getString(R.string.confirm)
      negativeButtonText = resources.getString(R.string.cancel)

      colorPicker {
          row = 5
          showAlphaView = false
      }
}.show(this)
```
#### Java
```java
AlertDialog dialog = new ColorPickerDialogBuilder(this)
      .setCancelable(true)
      .setPositiveButton(getResources().getString(R.string.confirm), (dialog, which) -> {})
      .setNegativeButton(getResources().getString(R.string.cancel), (dialog, which) -> {})
      .setRow(5)
      .alphaViewEnable(false)
      .show();
```
### Other 
Get callback when color changes:

Kotlin
```kotlin

colorPicker.onColorChanged = { color ->
     println("color:$color")
}

colorPicker.afterColorChanged = { color ->
     afterColorChanged(color)
     println("color:$color")
}

```

Java
```java
colorPicker.setOnColorSelectListener(new OnColorSelectListener() {
      @Override
      public void onColorChanged(@NonNull String color) {
          System.out.println(color);
      }
      
      @Override
      public void afterColorChanged(@NonNull String color) {
          System.out.println(color);
      }
});
```

## See [sample](https://github.com/panshen/GridColorPicker/tree/main/app/src/main/java/com/panshen/palette) for more usages.

INSTALL
-----
Add it in your root build.gradle:
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```
Add dependency in your module build.gradle:
```gradle
dependencies {
    implementation 'com.github.panshen:GridColorPicker:1.0.0'
}
```
