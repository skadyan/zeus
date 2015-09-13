{
   "settings" : {
       "index"   : {
          "number_of_shards"   : ${indexdef.number_of_shards},
          "number_of_replicas" : ${indexdef.number_of_replicas}
        },
        "entity" : "${index.type}"
    },
   
    "mappings" : {
      "${index.type}" : {
            "dynamic" : "strict",
            "_all"    : { "enabled": false },
            "_size"   : {"enabled" : true, "store" : true },
            "properties" : {
             <#list helper.indexableProperties as prop>
                  <@property p=prop /><#if prop_has_next>,</#if>
             </#list>
            }
        }  
    }
}
 
<#macro jsonValue v>${helper.writeJsonValue(v)}</#macro>
<#macro simpleProperty sp>
"${sp.name}" : {
    <#assign attrs = sp.searchIndexPropertyDef.settings >
    <#list attrs?keys as prop>
                      "${prop}" : <@jsonValue v=attrs[prop]> <#if prop_has_next>,
    </#if></#list>
   
                   }</#macro>
 
<#macro property p>
    <#if p.type == 'SIMPLE'>
       <@simpleProperty sp=p/>
    <#elseif p.type == 'COMPOSITE' || p.type == 'COMPOSITE_INLINE'  || p.type == 'REF'>
       <@compositeProperty cp=p/>
    <#elseif p.type == 'COLLECTION'>
       <@collectionProperty cp=p/>
   </#if>
</#macro>
 
<#macro collectionProperty cp>
   "${cp.name}" : {
       "type" : "nested",
       "properties" : {
     <#list findIndexableChildren(cp) as prop>
            <@property p=prop /><#if prop_has_next>,</#if>
     </#list>
       }
   }
</#macro>
 
<#macro compositeProperty cp>
   "${cp.name}" : {
     "type" : "object",
     "properties" : {
         <#list findIndexableChildren(cp) as prop>
                <@property p=prop /><#if prop_has_next>,</#if>
         </#list>
     }
   }
</#macro>