# GridColorPicker

<p align="center"> <img align="center" src="https://raw.githubusercontent.com/panshen/GridColorPicker/main/app/src/main/res/drawable/logo.png" width="80"/></p>
<p align="center"> <strong>一个安卓端灵活且优雅的颜色选色器</strong> </p>

<br>
<br>
<p align="center"> <img align="center" src="https://github.com/panshen/GridColorPicker/blob/main/screencapture/view_en.jpg" width="350"/></p>

中文 | [EN](https://github.com/panshen/GridColorPicker/blob/main/README_EN.md) 

用法
-----



### XML
使用 xml 声明一个 GridColorPicker，每个 gcp_xxx 属性都是可选的。
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

| 属性                    | 描述                                                         | 默认值            |
| :---------------------- | :----------------------------------------------------------- | ----------------- |
| gcp_row                 | 网格的行数，最小为2                                          | 10                |
| gcp_colorScheme         | 网格的颜色方案，最少需要定义两个颜色                         | #029FD6...#76BC40 |
| gcp_selectorColor       | 选择器的颜色资源                                             | #FFFFFF           |
| gcp_showAlphaView       | 是否显示 AlphaView                                           | true              |
| gcp_showAlphaViewLabel  | 是否显示 AlphaView 的标签。如果 showAlphaView 为 false，这个属性将被忽略。 | true              |
| gcp_alphaViewLabelColor | 标签的颜色资源                                               | #000000           |
| gcp_alphaViewLabelText  | AlphaView标签的文本                                          | Opacity           |
| gcp_drawCard            | 是否绘制 GridColorPicker 的卡片背景。                        | true              |
| gcp_cardColor           | 卡片的颜色资源                                               | #FDFDFD           |

### 动态创建颜色选择器
例如，创建一个有 5 行颜色且 AlphaView 被隐藏的 GridColorPicker：

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

### 创建颜色选择器 Dialog
为了方便使用，你可以创建一个包含 GridColorPicker 的 Dialog:
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
### 其他 
当颜色改变时获得回调:

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

## 更多用法请查看 [示例代码](https://github.com/panshen/GridColorPicker/tree/main/app/src/main/java/com/panshen/palette) 。

安装
-----
添加 jitpack 地址到项目级 build.gradle:
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```
添加项目依赖到模块的 build.gradle:
```gradle
dependencies {
    implementation 'com.github.panshen:GridColorPicker:1.0.1'
}
```

License
-------

    Copyright (c) 2022. panshen
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.