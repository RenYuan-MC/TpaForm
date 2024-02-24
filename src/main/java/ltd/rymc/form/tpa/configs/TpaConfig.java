package ltd.rymc.form.tpa.configs;

import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfHeader;
import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

import java.util.List;

@SuppressWarnings("unused")
@ConfHeader("# 配置文件 TpaForm Config ver 2.1.0 by RENaa_FD,xiantiao")
public interface TpaConfig {

    @ConfDefault.DefaultBoolean(false)
    @ConfKey("auto-completion-player-name")
    @ConfComments({
            "",
            "# 启用tpa玩家名称自动补全",
            "# 在遇到支持不完整玩家名的TPA插件时启用",
            "# 这会接管插件的名称补全",
            "# 此功能不影响表单内的玩家名称补全"
    })
    @AnnotationBasedSorter.Order(10)
    boolean completion();

    @ConfDefault.DefaultBoolean(true)
    @ConfKey("metrics")
    @ConfComments("\n# 启用数据统计")
    @AnnotationBasedSorter.Order(20)
    boolean metrics();

    @ConfDefault.DefaultBoolean(true)
    @ConfKey("redis.host")
    @ConfComments("\n# Redis的主机IP")
    @ConfDefault.DefaultString("localhost")
    @AnnotationBasedSorter.Order(21)
    String redisHost();

    @ConfDefault.DefaultBoolean(true)
    @ConfKey("redis.Port")
    @ConfComments("\n# Redis的主机端口")
    @ConfDefault.DefaultInteger(6379)
    @AnnotationBasedSorter.Order(22)
    int redisPort();@ConfDefault.DefaultBoolean(true)

    @ConfKey("redis.MaxTotal")
    @ConfComments("\n# 看不懂不要动")
    @ConfDefault.DefaultInteger(50)
    @AnnotationBasedSorter.Order(23)
    int redisMaxTotal();@ConfDefault.DefaultBoolean(true)

    @ConfKey("redis.MaxIdle")
    @ConfComments("\n# 看不懂不要动")
    @ConfDefault.DefaultInteger(50)
    @AnnotationBasedSorter.Order(24)
    int redisMaxIdle();

    @ConfKey("redis.User")
    @ConfComments("\n# Redis的用户，其他用户必须填写密码")
    @ConfDefault.DefaultString("default")
    @AnnotationBasedSorter.Order(25)
    String redisUser();

    @ConfKey("redis.Password")
    @ConfComments("\n# Redis的主机密码")
    @AnnotationBasedSorter.Order(26)
    String redisPassword();

    @ConfKey("redis.DB")
    @ConfComments("\n# Redis的主机数据库")
    @ConfDefault.DefaultInteger(0)
    @AnnotationBasedSorter.Order(27)
    int redisDB();

    @ConfDefault.DefaultBoolean(true)
    @ConfKey("enable-tpa-here")
    @ConfComments("\n# 启用TpaHere")
    @AnnotationBasedSorter.Order(28)
    boolean enableTpaHere();

    @ConfDefault.DefaultStrings({"tpa"})
    @ConfKey("tpa-commands")
    @ConfComments({
            "",
            "# tpa相关的命令",
            "# 接受和拒绝传送的命令支持使用%s指定玩家名",
            "# 示例: tpaccept %s"
    })
    @AnnotationBasedSorter.Order(30)
    List<String> tpaCommands();

    @ConfDefault.DefaultStrings({"tpahere"})
    @ConfKey("tpa-here-commands")
    @AnnotationBasedSorter.Order(40)
    List<String> tpaHereCommands();

    @ConfDefault.DefaultString("tpaccept")
    @ConfKey("tpa-accept-command")
    @AnnotationBasedSorter.Order(50)
    String tpaAcceptCommand();

    @ConfDefault.DefaultString("tpaccept")
    @ConfKey("tpa-here-accept-command")
    @AnnotationBasedSorter.Order(60)
    String tpaHereAcceptCommand();

    @ConfDefault.DefaultString("tpdeny")
    @ConfKey("tpa-deny-command")
    @AnnotationBasedSorter.Order(70)
    String tpaDenyCommand();

    @ConfDefault.DefaultString("tpdeny")
    @ConfKey("tpa-here-deny-command")
    @AnnotationBasedSorter.Order(80)
    String tpaHereDenyCommand();

    @ConfDefault.DefaultString("textures/ui/realms_green_check.png")
    @ConfKey("accept-form-icon")
    @ConfComments("\n# 接受传送的图标")
    @AnnotationBasedSorter.Order(90)
    String acceptIcon();

    @ConfDefault.DefaultString("textures/ui/realms_red_x.png")
    @ConfKey("deny-form-icon")
    @AnnotationBasedSorter.Order(100)
    String denyIcon();


}
