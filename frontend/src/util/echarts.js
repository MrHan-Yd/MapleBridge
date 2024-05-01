/* 引入charts核心模块， 核心模块提供 echarts使用必须的接口 */
import * as echarts from 'echarts/core';
/* 引入柱状图，折线图，饼图等图表 */
import { BarChart, LineChart, PieChart } from 'echarts/charts';

/* 引入提示框，标题，直角坐标系，数据集，内置数据转换组器组件， 组件后缀都为Component*/
import {
    TitleComponent,
    TooltipComponent,
    GridComponent,
    DatasetComponent,
    TransformComponent,
    ToolboxComponent,
    LegendComponent,
} from 'echarts/components';

/* 标签自动布局， 全局过度动画特性 */
import { LabelLayout, UniversalTransition } from 'echarts/features';

/* 引入Canvas渲染器， 驻日引入CanvasRenderer或者SVGRenderer 是必须的一步*/
import { CanvasRenderer } from 'echarts/renderers';

/* 注册必须的组件 */
echarts.use([
    TitleComponent,
    TooltipComponent,
    GridComponent,
    DatasetComponent,
    TransformComponent,
    ToolboxComponent,
    LegendComponent,
    BarChart,
    LineChart,
    PieChart,
    CanvasRenderer,
    LabelLayout,
    UniversalTransition,
]);

/* 导出 */
export default echarts;