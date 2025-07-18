#!/bin/bash

# 处理命令行参数
show_all=false
while [[ $# -gt 0 ]]; do
    case $1 in
        -a)
            show_all=true
            shift
            ;;
        *)
            echo "未知参数: $1" >&2
            exit 1
            ;;
    esac
done

# 使用 jps 列出所有 Java 进程
echo "正在查找 Java 进程..."
java_processes=$(jps -lv | grep -v Jps)

if [ -z "$java_processes" ]; then
    echo "未找到正在运行的 Java 进程!"
    exit 1
fi

# 生成带编号的进程列表
echo "找到以下 Java 进程:"
process_list=()
index=1
while IFS= read -r line; do
    pid=$(echo "$line" | awk '{print $1}')
    full_name=$(echo "$line" | cut -d' ' -f2-)

    if [ "$show_all" = true ]; then
        # 详细模式：显示完整信息（包括参数）
        name="$full_name"
    else
        # 简洁模式：只显示主类名或 JAR 文件名
        main_class=$(echo "$full_name" | awk '{print $1}')

        # 尝试提取 JAR 文件名（如果是 Spring Boot 应用）
        if echo "$main_class" | grep -q "JarLauncher"; then
            # 从参数中提取 JAR 名
            jar_name=$(echo "$full_name" | grep -oP '[-][-]spring\.main\.sources=\K[^ ]+')
            if [ -n "$jar_name" ]; then
                name=$(basename "$jar_name")
            else
                name="SpringBootApp"  # 无法提取时使用默认名称
            fi
        else
            # 普通 Java 类：只显示类名（去掉包路径）
            name=$(echo "$main_class" | awk -F. '{print $NF}')
        fi
    fi

    echo "[$index] PID: $pid, 进程: $name"
    # shellcheck disable=SC2004
    process_list[$index]=$pid
    ((index++))
done <<< "$java_processes"

# 获取用户选择
echo
# shellcheck disable=SC2162
read -p "请输入要操作的 Java 进程编号 [1-${#process_list[@]}]: " choice

# 验证选择是否有效
if ! [[ "$choice" =~ ^[0-9]+$ ]] || [ "$choice" -lt 1 ] || [ "$choice" -gt "${#process_list[@]}" ]; then
    echo "错误: 无效的选择!"
    exit 1
fi

# 获取对应的 PID
pid=${process_list[$choice]}
echo "已选择 PID: $pid"

# 分析进程堆栈后生成火焰图
# shellcheck disable=SC2086
echo "正在生成火焰图..."
mkdir -p ./temp
# 工具从 https://github.com/async-profiler/async-profiler 和 https://github.com/brendangregg/FlameGraph 中获取
./src/main/resources/hotgraph/work-async-profiler/bin/asprof -d 10 -o collapsed -f ./temp/hotgraph.txt $pid &&
./src/main/resources/hotgraph/work-flame-graph/flamegraph.pl ./temp/hotgraph.txt > ./temp/hotgraph.svg &&
echo "火焰图已生成, 位于 --> ./temp/hotgraph.svg"
