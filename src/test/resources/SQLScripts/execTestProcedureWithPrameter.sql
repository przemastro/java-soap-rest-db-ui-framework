DECLARE @procedureName nvarchar(50) = ?;
DECLARE @parameterName nvarchar(50) = ?;
DECLARE @parameterValue nvarchar(50) = ?;
DECLARE @query nvarchar(100) = 'exec '+@procedureName+' '+@parameterName+' = '+@parameterValue;
EXEC (@query);