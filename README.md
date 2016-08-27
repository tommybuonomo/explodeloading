#Explode Loading Animation

![ezgif com-video-to-gif](https://cloud.githubusercontent.com/assets/15737675/18030471/35565996-6cb7-11e6-9327-42ff7115651c.gif) ![ezgif com-video-to-gif 2](https://cloud.githubusercontent.com/assets/15737675/18030479/75e68184-6cb7-11e6-89a9-71271000d544.gif)
![explode3](https://cloud.githubusercontent.com/assets/15737675/18030477/506f28c0-6cb7-11e6-85b9-b675d6ebd164.gif)

##How to
####Gradle
```Gradle
dependencies {
    compile 'com.tbuonomo.explodeloading:explodeloading:1.0.0'
}
```
####In your XML layout
```Xml
<com.tbuonomo.explodeloading.ExplodeLoadingView
        android:layout_width="80dp"
        android:layout_height="80dp"
        android:layout_centerInParent="true"
        app:pointsNumber="20"
        app:pointsSize="5dp"
        app:pointsColor="#283593"
        app:animationDuration="2000"/>
```
####Attributes
| Attribute | Description |
| --- | --- |
| `pointsColor` | Color of the points |
| `pointsSize` | Size of the points in dp (by default 5dp) |
| `pointsNumber` | Number of exploded points |
| `animationDuration` | Step duration of the animation in ms (by default 1500) |

##License
    Copyright 2016 Tommy Buonomo
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
