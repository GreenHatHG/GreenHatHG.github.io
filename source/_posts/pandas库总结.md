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

