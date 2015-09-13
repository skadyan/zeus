<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper SYSTEM "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${model.code?cap_first}Mapper">
 
<#if helper.hasFeature('SourceHasEntityState') >
  <update id="updateItemState" parameterType="${stateUpdate.parameterType}">
     UPDATE ${stateUpdate.statusTable}
        SET ${stateUpdate.statusColumn} = ${r"#{state}"},
            ${stateUpdate.statusColumn} = CURRENT_TIMESTAMP
      WHERE <#list stateUpdate.updateStatusFilter as f> ${f.column} = ${r"#{"}${f.value}${r"}"}
<#if f_has_next>         AND </#if></#list>
   </update>
</#if>

<#if helper.isIntegrationModelIsPull()>     
    <select id="getPendingChangeNotifications" resultMap="pendingRunIds" resultOrdered="true" statementType="CALLABLE" parameterType="java.util.Map">
         {call ${helper.pendingChangeProvider} @ENTITY_CODE = '${model.code?upper_case}' , @BATCH_SIZE = ${r"#{batchSize}"}, @LOCK_EXPIRY_INTERVAL = ${r"#{lockExpiryInterval}"}  , @LOCK_OWNER = ${r"#{lockOwner}"} }
    </select>
    <resultMap id="pendingCN" type="java.util.LinkedHashMap">
    <#list helper.pendingChangeNotificationDataProperties as l>
          <result property="${l.name}" column="${l.source.alias}" />
    </#list>
    </resultMap>
</#if> 
 
<resultMap id="${model.code}ResultMap" type="${helper.typeName}">
    <#list helper.propertiesInSortedOrderOfMyBatis as prop>
        <@property p=prop />
    </#list>
</resultMap>
 
<#macro entitySql filter changeView>
      SELECT <#list helper.propertySources as source>
             ${source.nameOrExpr} [${source.alias}]<#if source_has_next>,</#if></#list>
        FROM ${source.sourceViewName}  ${source.sourceViewNameAlias}
<#if changeView >
  INNER JOIN ${source.changeViewName} ${source.changeViewNameAlias}
          ON <#list helper.identifierSources as id> ${source.sourceViewNameAlias}.[${helper.stripAliasPrefix(id.nameOrExpr)}] = ${source.changeViewNameAlias}.[${helper.stripAliasPrefix(id.nameOrExpr)}]
<#if id_has_next>         AND </#if></#list>
</#if>        
<#if filter.notEmpty()>
       WHERE <#list filter.list as f> ${f.source.nameOrExpr} = ${r"#{"}${f.name}${r"}"}
<#if f_has_next>         AND </#if></#list></#if>
    ORDER BY <#list helper.identifierSources as id> [${id.alias}]<#if id_has_next>, </#if></#list>
</#macro>
 
<select id="getChangedObjects" resultMap="${model.code}ResultMap" resultOrdered="true"  parameterType="java.util.Map">
      <@entitySql filter = helper.filter changeView = helper.isIntegrationModelIsPull() />
 </select>
 
<select id="getSpecificObject" resultMap="${model.code}ResultMap" resultOrdered="true"  parameterType="java.util.Map">
      <@entitySql filter = helper.specificFilter changeView = helper.isIntegrationModelIsPull() />
 </select>
 
 
<#macro simpleProperty sp>
       <${sp.source.identifier?string('id' , 'result')} property="${sp.name}" column="${sp.source.alias}" javaType="${sp.javaType.name}"/>
</#macro>
<#macro refProperty rp>
   <result property="${rp.name}.${rp.keyProperty.name}" column="${rp.keyProperty.source.alias}"  javaType="${rp.keyProperty.javaType.name}"/>
</#macro>
 
<#macro compositeProperty cp>
   <association property="${cp.name}" javaType="com.wellmanage.idd.model.EntityObject">
     <#list helper.nestedPropertiesInSortedOrderOfMyBatis(cp) as prop>
            <@property p=prop />
     </#list>
   </association>
</#macro>
 
<#macro collectionProperty cp>
   <collection  property="${cp.name}" javaType="java.util.ArrayList" ofType="com.wellmanage.idd.model.EntityObject">
     <#list helper.nestedPropertiesInSortedOrderOfMyBatis(cp) as prop>
            <@property p=prop />
     </#list>
   </collection>
</#macro>
 
<#macro property p>
    <#if p.type == 'SIMPLE' >
       <@simpleProperty sp=p/>
    <#elseif p.type == 'COMPOSITE' || p.type == 'COMPOSITE_INLINE' >
       <@compositeProperty cp=p/>
    <#elseif p.type == 'COLLECTION'>
       <@collectionProperty cp=p/>
    <#elseif p.type == 'REF' >
       <@refProperty rp=p/>
    </#if>
</#macro>    
</mapper>