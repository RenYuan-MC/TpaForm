package ltd.rymc.form.tpa.configs;

import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfHeader;
import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

@SuppressWarnings("unused")
@ConfHeader("# 语言文件 TpaForm Lang zh_cn ver 2.0.0 by RENaa_FD")
public interface LangConfig {
    @ConfDefault.DefaultString("§f[§6服务器§f] ")
    @ConfKey("prefix")
    @ConfComments("\n# 重载插件部分")
    @AnnotationBasedSorter.Order(10)
    String prefix();

    @ConfDefault.DefaultString("插件已重载！")
    @ConfKey("reload")
    @AnnotationBasedSorter.Order(20)
    String reload();

    @ConfDefault.DefaultString("传送菜单")
    @ConfKey("title")
    @ConfComments("\n# 菜单标题")
    @AnnotationBasedSorter.Order(30)
    String title();

    @ConfDefault.DefaultString("请选择玩家")
    @ConfKey("select-players")
    @ConfComments("\n# 传送选择菜单")
    @AnnotationBasedSorter.Order(40)
    String selectPlayers();

    @ConfDefault.DefaultString("玩家列表")
    @ConfKey("player-list")
    @AnnotationBasedSorter.Order(50)
    String playerList();

    @ConfDefault.DefaultString("如果上方找起来过于麻烦可以使用这个输入框")
    @ConfKey("input-tips")
    @AnnotationBasedSorter.Order(60)
    String inputTips();

    @ConfDefault.DefaultString("无需输入完整玩家名")
    @ConfKey("input-tips1")
    @AnnotationBasedSorter.Order(70)
    String inputTips1();

    @ConfDefault.DefaultString("模式(开为传送到你这,关为传送到你选择的玩家)")
    @ConfKey("tpa-mode")
    @AnnotationBasedSorter.Order(80)
    String tpaMode();

    @ConfDefault.DefaultString("%s 想要传送到你这")
    @ConfKey("tpa")
    @ConfComments("\n# 传送接受菜单")
    @AnnotationBasedSorter.Order(90)
    String tpa();

    @ConfDefault.DefaultString("%s 想要让你传送到他那")
    @ConfKey("tpa-here")
    @AnnotationBasedSorter.Order(100)
    String tpaHere();

    @ConfDefault.DefaultString("同意传送")
    @ConfKey("accept")
    @AnnotationBasedSorter.Order(110)
    String accept();

    @ConfDefault.DefaultString("拒绝传送")
    @ConfKey("deny")
    @AnnotationBasedSorter.Order(120)
    String deny();
}
