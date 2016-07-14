# ISexMusic
一款音乐播放器，功能有：

1.	本地音乐搜索
  主页界面先展示给用户，后台读取本地MP3文件：定义一个Mp3Info类：定义一个Mp3属性类，包含歌曲的各种属性（歌曲名称，歌曲时长等）；另外定义一个MediaUtil，用于从数据库中查询歌曲的信息，保存在List中，往List集合中添加Map对象数据，每一个Map对象存放一首音乐的所有属性；MusicListAdapter：自定义的音乐列表适配器。定义了一个handler，用于异步处理数据，新开一个线程，异步处理数据的结果通过handler返回

2.	界面设计

3.	歌曲播放（BackService部分）

4.	循环/随机处理

5.	侧边栏菜单
    1.侧边栏部分是采用一个模板式的自定义效果，主要是向左滑动以及点击某一张特定imageview滑出侧边栏。主要是在xml文件中引入需要的定义在activity中的滑出方式。把sidebar以及title文件封装后引入index.xml中。之后编辑sidebar可以更改侧滑栏布局，在index_activity中对sidebar中按钮定义和监听点击事件。
    2.侧滑栏的主要功能是定时提醒功能，我定义了edittext用来获取用户输入的内容，timepicker来选择事件，以及一个button开始定时，textview显示提示信息。其中用intent传递定时参数，dialog进行弹出显示提示信息。提供关闭功能从弹出dialog中返回当前activity。
    3.背景更改是定义了一个popupmenu来进行弹出菜单，也是用listener设置监听器，进行点击事件的反应。这里我用了一种可以算作投机取巧的方法，就是activity自带的一个getWindow().setBackgroundDrawableResource(R.drawable.bgb)的方法来设置背景。
 	
6.	歌词（未实现）
7.	界面变更特效开始的（Logo部分以及左右滑动部分）
8.	窗口小组件
    在widget_layout.xml文件里面设计界面创建xml文件夹以及widget_info文件后进行注册，然后在MusicWidget.java里面重写APPWidgetProvider类的onUpdate与onReceiver方法来使得小组件可以和BackService响应，从而对歌曲的后台播放进行控制 。
9.	播放列表（未实现）

