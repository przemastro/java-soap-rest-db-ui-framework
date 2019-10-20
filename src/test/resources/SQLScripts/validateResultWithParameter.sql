DECLARE @columnName nvarchar(50) = ?;
DECLARE @tableName nvarchar(50) = ?;
DECLARE @column nvarchar(50) = ?;
DECLARE @filterName nvarchar(50) = ?;
DECLARE @query nvarchar(100) = 'select top 1 '+@columnName+' from '+@tableName+' where '+@column+' = '+@filterName;
EXEC (@query);