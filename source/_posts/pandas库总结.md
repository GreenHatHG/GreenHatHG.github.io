---
title: pandas库总结
date: 2020-07-11 12:56:28
categories: python
tags: 
- python
- pandas
---

常见用法总结，方便查找

<!-- more -->

# 读取

```python
import pandas as pd

def read_csv():
    csv_path = sys.argv[1]
    is_file_empty = False if os.path.isfile(csv_path) and os.path.getsize(csv_path) > 0 else True
    if is_file_empty:
        raise SystemExit('File Empty')
    # 数据大时可能有内存溢出危险
    df = pd.read_excel(csv_path)
    # pd.read_excel('a.xlsx', sheet_name='sheet_name', encoding='utf-8-sig')
    return df
```

# 遍历

## 按行遍历iterrows

```python
for index, row in df.iterrows():
	print(index) # 输出每行的索引值
	print(row['模块'])
```

## 按行遍历apply

```python
def row_global_parameter(df_row):
    if not df_row['可用'] == '否':
        # 遍历每一列，v为该列的值
        for i, v in df_row.items():
            if i == '模块':
                print(v)
                
# 处理每一行，将每一行放到row_global_parameter处理
df.apply(row_global_parameter, axis=1)
```

## 特定几列遍历

```python
modules = df['模块']
names = df['名字']

for module, name in zip(module, names):
    print(module)
    print(name)
```

# 创建DataFrame

```python
columns = ['模块', '名字']
df = pd.DataFrame(columns=columns)

data = [1,2,3]
for item in data:
    # 插入一行
    df.loc[df.shape[0]] = [item, item]
```

# 新增列

```python
# 值为None（默认值）时，只有列名，没有数据
data['profession'] = None
```

# nan

## 删除所有的nan

```python
df.dropna(axis=0, how='all', inplace=True)
```

## 判断是不是nan

```
pd.isna(...)
```

## 替换所有的nan

```
df.fillna('', inplace=True)
```

# 合并去重

```python
df_list = [read_csv(), read_csv()]
all_pf = pd.concat(df_list)

# 需要去重复的列名 遇到重复的时保留第一个还是保留最后一个 去除重复项，还是保留重复项的副本
all_df.drop_duplicates(['模块'], keep='first', inplace=True)
all_df.drop_duplicates(subset=['模块', '接口'], keep='first', inplace=True)
```

# 更新一个sheet保持其他sheet内容不变

```python
def update_sheet(xlsx_file_path, update_sheet_name, df):
    # update one sheet data, but keep other sheet data
    book = load_workbook(xlsx_file_path)
    writer = pd.ExcelWriter(xlsx_file_path, engine='openpyxl')
    writer.book = book
    writer.sheets = dict((ws.title, ws) for ws in book.worksheets)
    df.to_excel(writer, update_sheet_name, index=None, encoding='utf-8-sig')
    writer.save()
    writer.close()  
```

# 根据df_a删除df_b一行

```python
for index, row in df_a.iterrows():
    if row['模块'] == 'a':
        # drop rows from df_b that contains row['模块'] string in a 模块 column
        df_b = df_b[~df_b['模块'].isin([row['模块']])]
```

# 创建excel

```python
writer = pd.ExcelWriter('excel/a.xlsx')
df_a.to_excel(writer, sheet_name='name1', encoding='utf-8', index=None)
df_b.to_excel(writer, sheet_name='name2', encoding='utf-8', index=None)
df_c.to_excel(writer, sheet_name='name3', encoding='utf-8', index=None)
writer.save()
writer.close()
```

# 删除

## 删除特定数值的行

```python
# 删除价格小于10000
df = df[ df['价格'] > 10000]
```

## 删除某列包含特定字符的行

```python
df[ ~ df['模块'].str.contains('a') ]
```

## 删除某一列

```python
df = df.drop('模块',axis=1)
```

## 删除某一行

```python
# 删除第3,4行，这里下表以0开始，并且标题行不算在类
df = df.drop([2, 3], axis=0)
```

# 对比两个excel

## 找出新excel[新增和修改]和[删除]的行

```python
merged = old_df.merge(new_df, indicator=True, how='outer')
diff_new_df = merged[merged['_merge'] == 'right_only']
```

这里`merged`用的上面那个，`left_only`其实是旧excel对比新excel所有的旧数据，所以我们要取一列为标准筛选出来，也就说选取那一列的值在旧的excel中有，新的没有，那么旧代表新的excel中删除了

```python
left_only = merged[merged['_merge'] == 'left_only']
diff_delete_df = left_only[~left_only['用例名称'].isin(diff_new_df['用例名称'])]
```

## 分别找出excel[新增] [修改] [删除]的行

这里对一行数据判定唯一的标准是用a列和b列

```python
diff_added_df = new_df[
            (~new_df.set_index(['a', 'b']).index.isin(outdated_df.set_index(['a', 'b']).index))]
```

```python
diff_removed_df = outdated_df[
            (~outdated_df.set_index(['a', 'b']).index.isin(new_df.set_index(['a', 'b']).index))]
```

```python
diff_changed_df = pd.DataFrame(columns=diff_removed_df.columns)
changed_rows = []
for index1, outdated_row in outdated_df.iterrows():
    for index2, new_row in new_df.iterrows():
        # pd.DataFrame.equals: The data type of columns of the two parameters must be the same before comparison
        if outdated_row['a'] == new_row['a'] and outdated_row['b'] == new_row[
            'b'] and not pd.DataFrame.equals(outdated_row.drop('ID'),
                                                new_row.drop('ID')):
            changed_rows.append(new_row.values)
            diff_changed_df = diff_changed_df.append(
                pd.DataFrame(changed_rows, columns=diff_changed_df.columns)).reset_index()
```

## 判断两个excel是否存在重复行

这里对一行数据判定唯一的标准是用a列和b列

```python
# Check the table for duplicate data(用例名称 用例目录 are the same at the same time)
def check_multiple_name_directory(df):
    res = df[df.duplicated(subset=['a', 'b'], keep=False)]
    return True if res.empty else False
```

# 改变列宽

```python
writer = pd.ExcelWriter(outfile, engine='xlsxwriter')
df.to_excel(writer, sheet_name=sheet_name, index=None)
_w = (10, 30, 10, 10, 10, 60, 30, 60, 20, 60, 20, 10, 10, 10)
for width, col in zip(_w, string.ascii_uppercase[:12]):
    writer.sheets[sheet_name].set_column(f'{col}:{col}', width)
```