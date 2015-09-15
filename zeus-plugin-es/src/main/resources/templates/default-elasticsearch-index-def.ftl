{
   "settings" : {
       "index"   : {
          "number_of_shards"   : ${indexdef.number_of_shards},
          "number_of_replicas" : ${indexdef.number_of_replicas}
        },
        "entity" : "${model.code}"
    },
   
    "mappings" : {
      "${model.code}" : {
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
    <#assign attrs = sp.index.definition/>
    <#list attrs?keys as prop>
       "${prop}" : <@jsonValue v=attrs[prop] /> <#if prop_has_next>, </#if>
    </#list>
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
     <#list helper.findIndexableChildren(cp) as prop>
            <@property p=prop /><#if prop_has_next>,</#if>
     </#list>
       }
   }
</#macro>
 
<#macro compositeProperty cp>
   "${cp.name}" : {
     "type" : "object",
     "properties" : {
         <#list helper.findIndexableChildren(cp) as prop>
                <@property p=prop /><#if prop_has_next>,</#if>
         </#list>
     }
   }
</#macro>