#!/usr/bin/env python3
"""
FastAPI for HSBC NL2SQL MVP
- /health: 健康检查
- /query: 同步查询（JSON）
- /stream: SSE 流式输出（逐步返回 Agent 思考/SQL/结果片段）

使用 LM Studio (OpenAI 兼容) 的本地模型 qwen/qwen3-30b-a3b-2507
"""

import asyncio
import json
import os
from typing import AsyncGenerator, Dict, Any

from fastapi import FastAPI, Request
from fastapi.responses import JSONResponse, HTMLResponse, StreamingResponse
from fastapi.staticfiles import StaticFiles

from hsbc_fund_sql_agent import nl2sql_query, build_agent, extract_sql_from_steps
import logging
import time
import uuid
from logging.handlers import RotatingFileHandler

# 结构化日志配置（输出到 logs 目录 + 控制台）- 统一日志文件
LOG_DIR = os.path.join(os.path.dirname(__file__), "logs")
os.makedirs(LOG_DIR, exist_ok=True)
LOG_FILE = os.path.join(LOG_DIR, "hsbc_fund_nl2sql.log")

_formatter = logging.Formatter("%(asctime)s | %(levelname)s | %(name)s | %(message)s")
_file_handler = RotatingFileHandler(LOG_FILE, maxBytes=5*1024*1024, backupCount=3, encoding="utf-8")
_file_handler.setFormatter(_formatter)
_console_handler = logging.StreamHandler()
_console_handler.setFormatter(_formatter)

_root = logging.getLogger("nl2sql")
_root.setLevel(logging.DEBUG)
if not _root.handlers:
    _root.addHandler(_file_handler)
    _root.addHandler(_console_handler)
_root.propagate = False

logger = logging.getLogger("nl2sql.api")

AGENT_TIMEOUT = int(os.getenv("AGENT_TIMEOUT", "60"))  # 秒
# 全局通知队列（用于Streamable HTTP的SSE通知通道）
NOTIFY_QUEUE: "asyncio.Queue" = asyncio.Queue(maxsize=1000)

async def push_notify(method: str, params: Dict[str, Any]) -> None:
    """以 JSON-RPC 通知的形式写入队列"""
    try:
        msg = {"jsonrpc": "2.0", "method": method, "params": params}
        await NOTIFY_QUEUE.put(msg)
    except Exception as e:
        logger.warning(f"push_notify error: {e}")


# 将复杂对象安全序列化为短字符串，避免 JSON 不可序列化错误
def _safe_preview(obj: Any, limit: int = 1000) -> str:
    try:
        if isinstance(obj, str):
            return obj[:limit]
        return (json.dumps(obj, ensure_ascii=False, default=str)[:limit])
    except Exception:
        try:
            return str(obj)[:limit]
        except Exception:
            return ""

# --- MCP/SSE 辅助工具（对齐 text-to-graphql-mcp 模式） ---

def _now_iso() -> str:
    try:
        from datetime import datetime
        return datetime.utcnow().strftime('%Y-%m-%dT%H:%M:%SZ')
    except Exception:
        return time.strftime('%Y-%m-%dT%H:%M:%SZ')


def _mk_progress_notification(request_id: str, progress: int, total: int, message: str, metadata: Dict[str, Any]) -> Dict[str, Any]:
    return {
        "jsonrpc": "2.0",
        "method": "notification/progress",
        "params": {
            "progressToken": f"progress_{request_id}_{int(time.time())}",
            "progress": int(progress),
            "total": int(total),
            "message": message,
            "metadata": metadata,
        },
    }




app = FastAPI(title="HSBC Fund NL2SQL MVP", version="2.0")

# 静态页面（前端测试）
STATIC_DIR = os.path.join(os.path.dirname(__file__), "static")
app.mount("/static", StaticFiles(directory=STATIC_DIR), name="static")


@app.get("/health")
async def health():
    logger.info("健康检查请求")
    return {"status": "ok"}


@app.get("/")
async def index():
    index_path = os.path.join(STATIC_DIR, "index.html")
    with open(index_path, "r", encoding="utf-8") as f:
        html = f.read()
    return HTMLResponse(html)


@app.post("/query")
async def query(payload: Dict[str, Any]):
    q = payload.get("query", "").strip()
    req_id = str(uuid.uuid4())
    if not q:
        logger.warning(f"[{req_id}] /query 空查询")
        return JSONResponse({"success": False, "error": "query is required"}, status_code=400)

    logger.info(f"[{req_id}] /query received | q={q}")
    t0 = time.perf_counter()

    async def run_agent() -> Dict[str, Any]:
        logger.debug(f"[{req_id}] 开始异步执行 Agent")
        return await asyncio.to_thread(nl2sql_query, q)

    try:
        result: Dict[str, Any] = await asyncio.wait_for(run_agent(), timeout=AGENT_TIMEOUT)
        dt = (time.perf_counter() - t0) * 1000
        logger.info(f"[{req_id}] /query done | ms={dt:.1f} | ok={result.get('success')}")
        return JSONResponse(result)
    except asyncio.TimeoutError:
        dt = (time.perf_counter() - t0) * 1000
        logger.error(f"[{req_id}] /query timeout | ms={dt:.1f}")
        return JSONResponse({"success": False, "error": "timeout"}, status_code=504)
    except Exception as e:
        dt = (time.perf_counter() - t0) * 1000
        logger.exception(f"[{req_id}] /query error | ms={dt:.1f} | err={e}")
        return JSONResponse({"success": False, "error": str(e)}, status_code=500)


async def sse_event_generator(q: str) -> AsyncGenerator[str, None]:
    """增强版 SSE：细粒度阶段推送 + 超时保护 + 详细日志。"""
    req_id = str(uuid.uuid4())
    t0 = time.perf_counter()
    yield f"data: {json.dumps({'phase': 'start', 'query': q, 'req_id': req_id})}\n\n"
    logger.info(f"[{req_id}] /stream start | q={q}")

    try:
        # Agent 初始化
        logger.debug(f"[{req_id}] 开始构建 Agent...")
        t1 = time.perf_counter()
        agent = await asyncio.to_thread(build_agent)
        agent_build_time = (time.perf_counter() - t1) * 1000
        yield f"data: {json.dumps({'phase': 'agent_ready', 'build_time_ms': round(agent_build_time, 1)})}\n\n"
        logger.info(f"[{req_id}] agent_ready | ms={agent_build_time:.1f}")

        async def run_agent():
            logger.debug(f"[{req_id}] 开始执行 Agent.invoke...")
            return await asyncio.to_thread(agent.invoke, {"input": q})

        # 执行 Agent，带超时
        logger.debug(f"[{req_id}] 调用 Agent，超时设置: {AGENT_TIMEOUT}s")
        t2 = time.perf_counter()
        result = await asyncio.wait_for(run_agent(), timeout=AGENT_TIMEOUT)
        agent_exec_time = (time.perf_counter() - t2) * 1000
        yield f"data: {json.dumps({'phase': 'agent_done', 'exec_time_ms': round(agent_exec_time, 1)})}\n\n"
        logger.info(f"[{req_id}] agent_done | ms={agent_exec_time:.1f}")

        # 推送 SQL（如有多条）
        logger.debug(f"[{req_id}] 提取 SQL 语句...")
        sqls = extract_sql_from_steps(result.get("intermediate_steps", []))
        logger.info(f"[{req_id}] 提取到 {len(sqls)} 条 SQL")
        for i, sql in enumerate(sqls, 1):
            chunk = {"phase": "sql", "index": i, "sql": sql}
            yield f"data: {json.dumps(chunk, ensure_ascii=False)}\n\n"
            logger.debug(f"[{req_id}] 推送 SQL {i}: {sql[:100]}...")
            await asyncio.sleep(0.02)

        # 推送最终答案
        out = result.get("output")
        total_time = (time.perf_counter() - t0) * 1000
        yield f"data: {json.dumps({'phase': 'final', 'output': out, 'total_time_ms': round(total_time, 1)}, ensure_ascii=False)}\n\n"
        logger.info(f"[{req_id}] /stream final | total_ms={total_time:.1f}")

    except asyncio.TimeoutError:
        total_time = (time.perf_counter() - t0) * 1000
        logger.error(f"[{req_id}] /stream timeout | total_ms={total_time:.1f}")
        yield f"data: {json.dumps({'phase': 'timeout', 'message': 'agent timeout', 'total_time_ms': round(total_time, 1)})}\n\n"
    except Exception as e:
        total_time = (time.perf_counter() - t0) * 1000
        logger.exception(f"[{req_id}] /stream error | total_ms={total_time:.1f} | err={e}")
        yield f"data: {json.dumps({'phase': 'error', 'message': str(e), 'total_time_ms': round(total_time, 1)})}\n\n"


@app.get("/stream")
async def stream(request: Request):
    q = request.query_params.get("q", "").strip()
    if not q:
        logger.warning("/stream 空查询参数")
        return JSONResponse({"success": False, "error": "q is required"}, status_code=400)

    logger.info(f"/stream 请求 | q={q}")

    async def event_stream():
        try:
            async for event in sse_event_generator(q):
                yield event
                # 客户端断开时停止
                if await request.is_disconnected():
                    logger.info("客户端断开连接，停止 SSE 流")
                    break
        except Exception as e:
            logger.exception(f"SSE 流异常: {e}")
            yield f"data: {json.dumps({'phase': 'error', 'message': str(e)})}\n\n"

    return StreamingResponse(event_stream(), media_type="text/event-stream")

# --- Minimal MCP Streamable HTTP endpoint for Inspector ---
# JSON-RPC 2.0 single-request handler implementing: initialize, tools/list, tools/call
@app.post("/mcp")
async def mcp_endpoint(request: Request):
    try:
        payload = await request.json()
    except Exception:
        return JSONResponse({"jsonrpc": "2.0", "error": {"code": -32700, "message": "Parse error"}, "id": None}, status_code=400)

    method = payload.get("method")
    rpc_id = payload.get("id")
    params = payload.get("params") or {}

    if method == "initialize":
        result = {
            "protocolVersion": "2024-11-05",
            "serverInfo": {"name": "hsbc-nl2sql", "version": "2.0"},
            "capabilities": {"tools": {"listChanged": False}},
        }
        return JSONResponse({"jsonrpc": "2.0", "id": rpc_id, "result": result})

    if method == "tools/list":
        tools = [
            {
                "name": "nl2sql.query",
                "description": "Run NL2SQL over hsbc_fund_unified and return natural language answer. SQL is returned in meta.sql.",
                "inputSchema": {
                    "type": "object",
                    "properties": {"query": {"type": "string", "description": "User question in natural language"}},
                    "required": ["query"],
                },
            }
        ]
        return JSONResponse({"jsonrpc": "2.0", "id": rpc_id, "result": {"tools": tools}})

    if method == "tools/call":
        name = params.get("name")
        arguments = params.get("arguments") or {}
        if name != "nl2sql.query":
            return JSONResponse({"jsonrpc": "2.0", "id": rpc_id, "error": {"code": -32601, "message": f"Unknown tool: {name}"}}, status_code=400)
        q = (arguments.get("query") or "").strip()
        if not q:
            return JSONResponse({"jsonrpc": "2.0", "id": rpc_id, "error": {"code": -32602, "message": "Missing 'query'"}}, status_code=400)

        # 读取 Inspector 的超时配置（若传递）
        req_timeout_ms = None
        max_total_timeout_ms = None
        try:
            headers = request.headers
            if "x-request-timeout" in headers:
                req_timeout_ms = int(headers.get("x-request-timeout"))
            if "x-maximum-total-timeout" in headers:
                max_total_timeout_ms = int(headers.get("x-maximum-total-timeout"))
        except Exception:
            pass
        timeout_candidates = []
        if req_timeout_ms:
            timeout_candidates.append(req_timeout_ms / 1000.0)
        if max_total_timeout_ms:
            timeout_candidates.append(max_total_timeout_ms / 1000.0)
        timeout_sec = min(timeout_candidates) if timeout_candidates else AGENT_TIMEOUT

        # 立即推送一条通知，便于 Inspector 的 Server Notifications 面板看到事件
        try:
            await push_notify("nl2sql/call_received", {"query": q, "timeout_sec": timeout_sec})
        except Exception:
            pass

        accept = (request.headers.get("accept") or "").lower()
        wants_sse = "text/event-stream" in accept

        if wants_sse:
            async def sse_stream():
                try:
                    # start
                    start_evt = {'jsonrpc':'2.0','method':'nl2sql/start','params':{'query': q}}
                    yield f"data: {json.dumps(start_evt, ensure_ascii=False)}\n\n"
                    try:
                        await push_notify('nl2sql/start', {'query': q})
                    except Exception:
                        pass

                    # 构建 agent
                    agent = await asyncio.to_thread(build_agent)
                    agent_ready_evt = {'jsonrpc':'2.0','method':'nl2sql/agent_ready','params':{}}
                    yield f"data: {json.dumps(agent_ready_evt, ensure_ascii=False)}\n\n"
                    try:
                        await push_notify('nl2sql/agent_ready', {})
                    except Exception:
                        pass

                    # 实时事件流（直接在当前生成器内产出 SSE）
                    sqls = []
                    final_text = None

                    # 事件映射为标准事件 + MCP进度通知
                    start_time = time.perf_counter()
                    last_evt = start_time
                    step_counter = 0
                    current_progress = 20
                    total_progress = 100

                    # 初始进度通知
                    msg = _mk_progress_notification(str(rpc_id), current_progress, total_progress,
                                                    "Agent initialized", {
                                                        "event_type": "initialized",
                                                        "timestamp": _now_iso(),
                                                        "request_id": rpc_id,
                                                    })
                    yield "event: message\n"
                    yield f"data: {json.dumps(msg, ensure_ascii=False)}\n\n"

                    try:
                        async for ev in agent.astream_events({"input": q}, version="v1"):
                            et = ev.get("event")
                            data = ev.get("data") or {}

                            # Heartbeat if idle > 3s
                            now = time.perf_counter()
                            if now - last_evt > 3:
                                hb = {"timestamp": _now_iso(), "status": "alive"}
                                yield "event: heartbeat\n"
                                yield f"data: {json.dumps(hb, ensure_ascii=False)}\n\n"
                                try:
                                    await push_notify('nl2sql/heartbeat', hb)
                                except Exception:
                                    pass
                                last_evt = now

                            if et == "on_llm_start":
                                evt = {
                                    "type": "llm_call_started",
                                    "step_name": data.get("name") or "llm",
                                    "message": "LLM 调用开始",
                                    "timestamp": _now_iso(),
                                }
                                yield "event: llm_call_started\n"
                                yield f"data: {json.dumps(evt, ensure_ascii=False)}\n\n"
                                current_progress = min(current_progress + 2, 95)
                                meta = {"event_type": "llm_call_started", "timestamp": evt["timestamp"], "request_id": rpc_id}
                                pn = _mk_progress_notification(str(rpc_id), current_progress, total_progress, evt["message"], meta)
                                yield "event: message\n"
                                yield f"data: {json.dumps(pn, ensure_ascii=False)}\n\n"
                                last_evt = time.perf_counter()

                            elif et == "on_llm_stream":
                                chunk = data.get("chunk")
                                evt = {
                                    "type": "llm_call_progress",
                                    "step_name": data.get("name") or "llm",
                                    "message": str(chunk)[:200],
                                    "timestamp": _now_iso(),
                                }
                                yield "event: llm_call_progress\n"
                                yield f"data: {json.dumps(evt, ensure_ascii=False)}\n\n"
                                last_evt = time.perf_counter()

                            elif et == "on_llm_end":
                                evt = {
                                    "type": "llm_call_completed",
                                    "step_name": data.get("name") or "llm",
                                    "message": "LLM 调用完成",
                                    "timestamp": _now_iso(),
                                }
                                yield "event: llm_call_completed\n"
                                yield f"data: {json.dumps(evt, ensure_ascii=False)}\n\n"
                                current_progress = min(current_progress + 3, 96)
                                meta = {"event_type": "llm_call_completed", "timestamp": evt["timestamp"], "request_id": rpc_id}
                                pn = _mk_progress_notification(str(rpc_id), current_progress, total_progress, evt["message"], meta)
                                yield "event: message\n"
                                yield f"data: {json.dumps(pn, ensure_ascii=False)}\n\n"
                                last_evt = time.perf_counter()

                            elif et == "on_tool_start":
                                step_counter += 1
                                tool_name = data.get("name")
                                step_name = "query_execution" if str(tool_name).lower().find("sql") >= 0 else "query_construction"
                                params = {
                                    "type": "step_started",
                                    "step_name": step_name,
                                    "step_number": step_counter,
                                    "message": f"Starting step: {step_name}",
                                    "timestamp": _now_iso(),
                                    "input": _safe_preview(data.get("input")),
                                }
                                yield "event: step_started\n"
                                yield f"data: {json.dumps(params, ensure_ascii=False)}\n\n"
                                try:
                                    await push_notify('nl2sql/step_started', params)
                                except Exception:
                                    pass
                                current_progress = min(current_progress + 5, 97)
                                meta = {"event_type": "step_started", "timestamp": params["timestamp"], "request_id": rpc_id,
                                        "step_name": step_name, "step_number": step_counter}
                                pn = _mk_progress_notification(str(rpc_id), current_progress, total_progress, params["message"], meta)
                                yield "event: message\n"
                                yield f"data: {json.dumps(pn, ensure_ascii=False)}\n\n"
                                # 粗提取 SQL（兼容 LangChain 事件 data.input 为 dict 的情况）
                                inp = data.get("input")
                                try:
                                    candidate = None
                                    if isinstance(inp, str):
                                        candidate = inp
                                    elif isinstance(inp, dict):
                                        # 常见字段：query/sql/queries[0]
                                        if isinstance(inp.get("query"), str):
                                            candidate = inp.get("query")
                                        elif isinstance(inp.get("sql"), str):
                                            candidate = inp.get("sql")
                                        elif isinstance(inp.get("queries"), list) and inp.get("queries"):
                                            first = inp.get("queries")[0]
                                            if isinstance(first, str):
                                                candidate = first
                                            elif isinstance(first, dict):
                                                candidate = first.get("query") or first.get("sql")
                                    if isinstance(candidate, str):
                                        low = candidate.lower()
                                        if "select " in low or "with " in low:
                                            sqls.append(candidate)
                                except Exception:
                                    pass
                                last_evt = time.perf_counter()

                            elif et == "on_tool_end":
                                tool_name = data.get("name")
                                step_name = "query_execution" if str(tool_name).lower().find("sql") >= 0 else "query_construction"
                                params = {
                                    "type": "step_completed",
                                    "step_name": step_name,
                                    "step_number": step_counter,
                                    "message": f"Completed step: {step_name}",
                                    "timestamp": _now_iso(),
                                    "output": _safe_preview(data.get("output")),
                                }
                                yield "event: step_completed\n"
                                yield f"data: {json.dumps(params, ensure_ascii=False)}\n\n"
                                try:
                                    await push_notify('nl2sql/step_completed', params)
                                except Exception:
                                    pass
                                current_progress = min(current_progress + 7, 98)
                                meta = {"event_type": "step_completed", "timestamp": params["timestamp"], "request_id": rpc_id,
                                        "step_name": step_name, "step_number": step_counter}
                                pn = _mk_progress_notification(str(rpc_id), current_progress, total_progress, params["message"], meta)
                                yield "event: message\n"
                                yield f"data: {json.dumps(pn, ensure_ascii=False)}\n\n"
                                last_evt = time.perf_counter()

                            elif et == "on_chain_end":
                                # 最终输出（使用安全序列化，防止 AgentAction 等类型导致 JSON 序列化异常）
                                out = data.get("output")
                                try:
                                    if isinstance(out, dict):
                                        final_text = out.get("output") or out.get("result")
                                        if final_text is None:
                                            final_text = _safe_preview(out)
                                    else:
                                        final_text = _safe_preview(out)
                                except Exception:
                                    final_text = _safe_preview(out)

                                evt = {
                                    "type": "workflow_completed",
                                    "message": "Agent 推理完成",
                                    "timestamp": _now_iso(),
                                }
                                yield "event: workflow_completed\n"
                                yield f"data: {json.dumps(evt, ensure_ascii=False)}\n\n"
                                meta = {"event_type": "workflow_completed", "timestamp": evt["timestamp"], "request_id": rpc_id}
                                pn = _mk_progress_notification(str(rpc_id), 100, total_progress, evt["message"], meta)
                                yield "event: message\n"
                                yield f"data: {json.dumps(pn, ensure_ascii=False)}\n\n"
                                last_evt = time.perf_counter()
                                # 已完成，跳出事件循环，避免重复 on_chain_end
                                break

                            # 软超时
                            if (time.perf_counter() - start_time) > timeout_sec:
                                raise asyncio.TimeoutError()
                    except asyncio.TimeoutError:
                        raise

                    # 最终响应 + 关闭
                    text = final_text or ""
                    content = [{"type": "text", "text": text}]
                    response_obj = {"jsonrpc": "2.0", "id": rpc_id, "result": {"content": content, "isError": False, "meta": {"sql": sqls}}}

                    # 最终响应：按 MCP Streamable HTTP 规范（Inspector 习惯监听 event: message）
                    yield "event: message\n"
                    yield f"data: {json.dumps(response_obj, ensure_ascii=False)}\n\n"
                    try:
                        await push_notify('nl2sql/final', {'text': text, 'total_sql': len(sqls)})
                    except Exception:
                        pass
                except asyncio.TimeoutError:
                    err = {"jsonrpc":"2.0","id": rpc_id, "error":{"code": -32001, "message":"Request timed out"}}
                    # SSE error event + MCP-compatible message frame
                    yield "event: error\n"
                    yield f"data: {json.dumps(err, ensure_ascii=False)}\n\n"
                    yield "event: message\n"
                    yield f"data: {json.dumps(err, ensure_ascii=False)}\n\n"
                    try:
                        await push_notify('nl2sql/error', {'message': 'Request timed out'})
                    except Exception:
                        pass
                except Exception as e:
                    err = {"jsonrpc":"2.0","id": rpc_id, "error":{"code": -32001, "message": str(e)}}
                    # SSE error event + MCP-compatible message frame
                    yield "event: error\n"
                    yield f"data: {json.dumps(err, ensure_ascii=False)}\n\n"
                    yield "event: message\n"
                    yield f"data: {json.dumps(err, ensure_ascii=False)}\n\n"
                    try:
                        await push_notify('nl2sql/error', {'message': str(e)})
                    except Exception:
                        pass
            return StreamingResponse(
                sse_stream(),
                media_type="text/event-stream",
                headers={
                    "Cache-Control": "no-cache",
                    "Connection": "keep-alive",
                    "X-Accel-Buffering": "no",
                    "Transfer-Encoding": "chunked",
                    "Content-Encoding": "identity",
                    "Access-Control-Allow-Origin": "*",
                    "Access-Control-Allow-Headers": "*",
                    "Access-Control-Allow-Methods": "GET, POST, OPTIONS",
                },
            )

        # 非流式：一次性 JSON 返回
        try:
            result = await asyncio.wait_for(asyncio.to_thread(nl2sql_query, q), timeout=timeout_sec)
        except asyncio.TimeoutError:
            return JSONResponse({"jsonrpc": "2.0", "id": rpc_id, "error": {"code": -32001, "message": "Request timed out"}}, status_code=504)
        except Exception as e:
            return JSONResponse({"jsonrpc": "2.0", "id": rpc_id, "error": {"code": -32001, "message": str(e)}}, status_code=500)

        text = result.get("output") or ""
        sqls = result.get("sql") or []
        content = [{"type": "text", "text": text}]

        # 将步骤与最终结果发到通知流（供 Inspector Server Notifications 面板显示）
        try:
            steps = result.get("intermediate_steps", [])
            await push_notify("nl2sql/steps", {"count": len(steps)})
            for i, step in enumerate(steps, 1):
                action, observation = step[0], step[1]
                await push_notify("nl2sql/step", {
                    "index": i,
                    "action": str(action)[:2000],
                    "observation": str(observation)[:2000]
                })
                try:
                    sqls_step = extract_sql_from_steps([step])
                    if sqls_step:
                        await push_notify("nl2sql/sql", {"index": i, "sql": sqls_step[0]})
                except Exception:
                    pass
            await push_notify("nl2sql/final", {"text": text, "total_sql": len(sqls)})
        except Exception as _:
            pass

        return JSONResponse({"jsonrpc": "2.0", "id": rpc_id, "result": {"content": content, "isError": False, "meta": {"sql": sqls}}})

    if method == "ping":
        return JSONResponse({"jsonrpc": "2.0", "id": rpc_id, "result": {"ok": True}})


# --- Streamable HTTP: GET /mcp for server-initiated notifications (SSE) ---
@app.get("/mcp")
async def mcp_notifications_stream():
    async def gen():
        while True:
            msg = await NOTIFY_QUEUE.get()
            yield f"data: {json.dumps(msg, ensure_ascii=False)}\n\n"
    return StreamingResponse(gen(), media_type="text/event-stream")

# --- MCP SSE (Server-Sent Events) streaming endpoint ---
# Inspector可通过 transport=sse & serverUrl=http://localhost:8080/mcp-sse 连接
@app.get("/mcp-sse")
async def mcp_sse(request: Request):
    """
    将 NL2SQL 的执行过程以 MCP Inspector 兼容的通知事件流式输出：
    - initialize 省略，直接等待 tools/call 样式的 JSON 在 query 参数 input 中传入
    - 为便于使用，这里简化：用 q= 自然语言问题 直接触发执行
    - 事件格式：以 JSON-RPC style 的 progress 通知为蓝本，输出 step/observation/sql/final
    """
    q = request.query_params.get("q", "").strip()
    if not q:
        return JSONResponse({"error": "missing q"}, status_code=400)

    logger.info(f"/mcp-sse start | q={q}")

    async def event_stream():
        req_id = str(uuid.uuid4())
        t0 = time.perf_counter()
        try:
            # 构建Agent
            yield f"data: {json.dumps({'event':'start','req_id':req_id,'q':q})}\n\n"

            agent = await asyncio.to_thread(build_agent)
            yield f"data: {json.dumps({'event':'agent_ready'})}\n\n"

            # 调用并捕获中间步骤
            def run():
                res = agent.invoke({"input": q})
                return res

            result = await asyncio.to_thread(run)

            # 推送中间步骤
            steps = result.get("intermediate_steps", [])
            for i, step in enumerate(steps, 1):
                try:
                    action, observation = step[0], step[1]
                    action_repr = str(action)
                    obs_repr = str(observation)
                    yield f"data: {json.dumps({'event':'step','index':i,'action':action_repr[:2000],'observation':obs_repr[:2000]})}\n\n"
                    # 从该步提取SQL
                    try:
                        from hsbc_fund_sql_agent import extract_sql_from_steps
                        sqls = extract_sql_from_steps([step])
                        if sqls:
                            yield f"data: {json.dumps({'event':'sql','index':i,'sql':sqls[0]})}\n\n"
                    except Exception:
                        pass
                    await asyncio.sleep(0)
                except Exception:
                    continue

            # 最终输出
            total_ms = (time.perf_counter() - t0) * 1000
            yield f"data: {json.dumps({'event':'final','output':result.get('output'),'total_time_ms':round(total_ms,1)})}\n\n"
        except Exception as e:
            yield f"data: {json.dumps({'event':'error','message':str(e)})}\n\n"

    return StreamingResponse(event_stream(), media_type="text/event-stream")


# --- MCP Streamable HTTP Notifications endpoint (SSE) ---
# MCP Inspector will open serverUrl + "/notifications" for server-initiated events
@app.get("/mcp/notifications")
async def mcp_notifications():
    async def gen():
        while True:
            msg = await NOTIFY_QUEUE.get()
            yield f"data: {json.dumps(msg, ensure_ascii=False)}\n\n"
    return StreamingResponse(gen(), media_type="text/event-stream")



if __name__ == "__main__":
    import uvicorn

    port = int(os.getenv("PORT", "8080"))
    uvicorn.run(app, host="0.0.0.0", port=port)

